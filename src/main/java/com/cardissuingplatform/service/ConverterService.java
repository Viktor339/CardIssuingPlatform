package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConverterService {
    private final CardMapperService cardMapperService;

    public GetCardResponse cardStatusToGetCardResponse(CardStatus cardStatus) {

        Card card = cardStatus.getCard();

        return GetCardResponse.builder()
                .id(card.getId())
                .type(GetCardResponse.Type.valueOf(card.getType().name()))
                .validTill(card.getValidTill())
                .firstName(card.getFirstName())
                .lastName(card.getLastName())
                .isActive(card.getIsActive())
                .currentStatus(cardMapperService.getStatus(cardStatus.getStatus()))
                .currency(cardMapperService.getCurrency(card.getCurrency()))
                .build();
    }
}
