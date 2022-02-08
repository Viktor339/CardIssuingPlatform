package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.model.Card_;
import com.cardissuingplatform.repository.CardStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cardissuingplatform.repository.specification.CardSpecification.withActive;
import static com.cardissuingplatform.repository.specification.CardSpecification.withCardStatus;
import static com.cardissuingplatform.repository.specification.CardSpecification.withCurrency;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardStatusRepository cardStatusRepository;
    private final CardMapperService cardMapperService;

    public List<GetCardResponse> get(GetCardDto getCardDto) {

        Specification<CardStatus> spec = where(withCardStatus(getCardDto.getStatus()))
                .and(withCurrency(getCardDto.getCurrency()))
                .and(withActive(getCardDto.getIsActive()));

        List<Sort.Order> orders = new ArrayList<>();
        if (getCardDto.getSortLastName() != null) {
            if (getCardDto.getSortLastNameBy().equals("desc")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, Card_.LAST_NAME));
            }
            orders.add(new Sort.Order(Sort.Direction.ASC, Card_.LAST_NAME));
        }

        if (getCardDto.getSortName() != null) {
            if (getCardDto.getSortNameBy().equals("desc")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, Card_.FIRST_NAME));
            }
            orders.add(new Sort.Order(Sort.Direction.ASC, Card_.FIRST_NAME));
        }

        Pageable pageable = PageRequest.of(getCardDto.getPage(), getCardDto.getSize(), Sort.by(orders));

        List<GetCardResponse> result = new ArrayList<>();
        cardStatusRepository.findAll(spec, pageable).getContent()
                .forEach(cardStatus -> {
                    Card card = cardStatus.getCard();
                    result.add(GetCardResponse.builder()
                            .id(card.getId())
                            .type(GetCardResponse.Type.valueOf(card.getType().name()))
                            .validTill(card.getValidTill())
                            .firstName(card.getFirstName())
                            .lastName(card.getLastName())
                            .isActive(card.getIsActive())
                            .currentStatus(cardMapperService.getStatus(cardStatus.getStatus()))
                            .currency(cardMapperService.getCurrency(card.getCurrency()))
                            .build());
                });
        return result;
    }
}
