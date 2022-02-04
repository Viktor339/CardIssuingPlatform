package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapperService cardMapperService;

    public List<GetCardResponse> get(GetCardDto getCardDto) {

        return cardRepository.findByGetCardDto(getCardDto)
                .stream()
                .peek(card ->{
                            card.setCurrency(cardMapperService.getCurrency(card.getCurrency()));
                            card.setCurrentStatus(cardMapperService.getStatus(card.getCurrentStatus()));
                        }).collect(Collectors.toList());
    }
}
