package com.cardissuingplatform.controller;

import com.cardissuingplatform.config.PageProperties;
import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.service.CardService;
import com.cardissuingplatform.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<GetCardResponse> get(@RequestParam(name = "sortName", required = false) String sortName,
                                     @RequestParam(name = "sortNameBy", defaultValue = "asc") String sortNameBy,
                                     @RequestParam(name = "sortLastName", required = false) String sortLastName,
                                     @RequestParam(name = "sortLastNameBy", defaultValue = "asc") String sortLastNameBy,

                                     @RequestParam(name = "currency", required = false) String currency,
                                     @RequestParam(name = "status", required = false) String status,
                                     @RequestParam(name = "isActive", required = false) Boolean active,

                                     @RequestParam(name = "size", defaultValue = "10") Integer size,
                                     @RequestParam(name = "page") Integer page) {

        Integer validatedSize = pageService.validatePageSize(size, pageProperties.getMin(), pageProperties.getMax());

        return cardService.get(
                GetCardDto.builder()
                        .sortName(sortName)
                        .sortNameBy(sortNameBy)
                        .sortLastName(sortLastName)
                        .sortLastNameBy(sortLastNameBy)
                        .currency(currency)
                        .status(status)
                        .active(active)
                        .size(validatedSize)
                        .page(page)
                        .build());

    }
}
