package com.cardissuingplatform.service.listener;

import com.cardissuingplatform.config.MessageBrokerProperties;
import com.cardissuingplatform.controller.dto.CardStatusRequestQueueMessage;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.repository.CardRepository;
import com.cardissuingplatform.repository.CardStatusRepository;
import com.cardissuingplatform.service.exception.CardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@EnableRabbit
@Component
@RequiredArgsConstructor
public class CardStatusQueueListener {

    private final CardStatusRepository cardStatusRepository;
    private final CardRepository cardRepository;

    @RabbitListener(queues = MessageBrokerProperties.CARD_STATUS_QUEUE)
    public void processQueue(CardStatusRequestQueueMessage message) {

        Long cardId = message.getId();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        List<CardStatus> cardStatuses = cardStatusRepository.findAllCardStatusByCardId(cardId);

        Optional<CardStatus> cardStatus = cardStatuses.stream()
                .filter(cs -> cs.getCreated().compareTo(message.getCreated()) > 0)
                .findFirst();

        if (cardStatus.isEmpty()) {
            cardStatusRepository.save(CardStatus.builder()
                    .status(message.getStatus())
                    .previousStatus("100")
                    .card(card)
                    .created(Instant.now())
                    .build());
        }
    }
}
