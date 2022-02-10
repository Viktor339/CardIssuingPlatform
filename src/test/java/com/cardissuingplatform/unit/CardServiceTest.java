package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.repository.CardStatusRepository;
import com.cardissuingplatform.service.CardService;
import com.cardissuingplatform.service.ConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardStatusRepository cardStatusRepository;

    @Mock
    private ConverterService converterService;

    @InjectMocks
    private CardService cardService;

    private GetCardResponse getCardResponse;
    private GetCardDto getCardDto;
    private final List<GetCardResponse> list = new ArrayList<>();
    private PageImpl<CardStatus> pagedResponse;

    @BeforeEach
    public void init() {

        getCardDto = GetCardDto.builder()
                .status("100")
                .page(0)
                .size(1)
                .build();

        getCardResponse = GetCardResponse.builder()
                .id(2L)
                .type(GetCardResponse.Type.PERSONAL)
                .validTill(Instant.parse("2022-02-03T21:00:00Z"))
                .firstName("Bready")
                .lastName("Pitt")
                .isActive(true)
                .currency("Евро")
                .build();

        list.add(getCardResponse);

        Card card = Card.builder()
                .id(1L)
                .createdBy(1)
                .build();

        List<CardStatus> cardStatusList = List.of(CardStatus.builder()
                .id(1L)
                .created(Instant.now())
                .previousStatus("200")
                .card(card).build());

        pagedResponse = new PageImpl<>(cardStatusList);

    }

    @Test
    public void testGet() {

        when(cardStatusRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(pagedResponse);
        when(converterService.cardStatusToGetCardResponse(Mockito.any(CardStatus.class))).thenReturn(getCardResponse);

        assertEquals(list, cardService.get(getCardDto));
    }
}
