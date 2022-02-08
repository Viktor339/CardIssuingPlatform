package com.cardissuingplatform.controller;

import com.cardissuingplatform.config.PageProperties;
import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.service.CardService;
import com.cardissuingplatform.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accountant/v1/cards")
public class CardController {

    private final CardService cardService;
    private final PageService pageService;
    private final PageProperties pageProperties;

    @GetMapping
    public List<GetCardResponse> get(GetCardDto getCardDto) {

        Integer validatedSize = pageService.validatePageSize(getCardDto.getSize(), pageProperties.getMin(), pageProperties.getMax());

        getCardDto.setSize(validatedSize);
        return cardService.get(getCardDto);
    }
}
