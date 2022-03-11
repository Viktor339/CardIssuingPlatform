package com.cardissuingplatform.service;

import com.cardissuingplatform.config.MessageBrokerProperties;
import com.cardissuingplatform.controller.dto.CardRequestQueueMessage;
import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.dto.TokenDto;
import com.cardissuingplatform.controller.request.ProceedRequest;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.controller.response.ProceedResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.CardRepository;
import com.cardissuingplatform.repository.CardStatusRepository;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static com.cardissuingplatform.repository.specification.CardSpecification.withActive;
import static com.cardissuingplatform.repository.specification.CardSpecification.withCardStatus;
import static com.cardissuingplatform.repository.specification.CardSpecification.withCurrency;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardStatusRepository cardStatusRepository;
    private final ConverterService converterService;
    private final OrderBuilderService orderBuilderService;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final AmqpTemplate rabbitTemplate;

    public List<GetCardResponse> get(GetCardDto getCardDto) {

        Specification<CardStatus> spec = where(withCardStatus(getCardDto.getStatus()))
                .and(withCurrency(getCardDto.getCurrency()))
                .and(withActive(getCardDto.getIsActive()));

        List<Sort.Order> orders = orderBuilderService.buildCardServiceOrder(getCardDto);

        Pageable pageable = PageRequest.of(getCardDto.getPage(), getCardDto.getSize(), Sort.by(orders));

        return cardStatusRepository.findAll(spec, pageable).getContent()
                .stream()
                .map(converterService::cardStatusToGetCardResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProceedResponse proceed(ProceedRequest proceedRequest, TokenDto tokenDto) {

        User owner = userRepository.findUserById(proceedRequest.getOwnedBy())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Card card = cardRepository.save(Card.builder()
                .type(Card.Type.valueOf(proceedRequest.getType().name()))
                .currency(proceedRequest.getCurrency().toString())
                .ownedBy(owner)
                .isActive(true)
                .lastName(proceedRequest.getLastName())
                .firstName(proceedRequest.getFirstName())
                .validTill(proceedRequest.getValidTill().atStartOfDay(ZoneId.systemDefault()).toInstant())
                .number(proceedRequest.getNumber().toString())
                .currency(proceedRequest.getCurrency().toString())
                .createdBy(tokenDto.getUserId().intValue())
                .company(owner.getCompany())
                .build()
        );

        CardStatus cardStatus = cardStatusRepository.save(CardStatus.builder()
                .card(card)
                .created(Instant.now())
                .status("100")
                .build());

        rabbitTemplate.convertAndSend(MessageBrokerProperties.CARD_REQUEST_QUEUE, CardRequestQueueMessage.builder()
                .id(cardStatus.getId())
                .currency(proceedRequest.getCurrency())
                .lastName(proceedRequest.getLastName())
                .firstName(proceedRequest.getFirstName())
                .number(proceedRequest.getNumber())
                .build());

        return ProceedResponse.builder()
                .id(card.getId())
                .build();
    }
}
