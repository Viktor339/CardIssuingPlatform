package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.response.GetAuthorityResponse;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Authority;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConverterService {
    private final CardMapperService cardMapperService;
    private final AuthorityRepository authorityRepository;

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

    public GetAuthorityResponse userToGetAuthorityResponse(User user) {

        List<Authority> userAuthority = authorityRepository.findAllByUser(user);

        return GetAuthorityResponse.builder()
                .userId(user.getId())
                .authorities(
                        userAuthority.stream()
                                .filter(Objects::nonNull)
                                .map(authority -> authority.getAuthorityName().name())
                                .collect(Collectors.toList()))
                .build();
    }
}
