package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.service.CardMapperService;
import com.cardissuingplatform.service.ConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ConverterServiceTest {

    @InjectMocks
    private ConverterService converterService;

    @Mock
    private CardMapperService cardMapperService;

    private CardStatus cardStatus;
    private GetCardResponse getCardResponse;

    @BeforeEach
    public void init() {
        Card card = Card.builder()
                .id(1L)
                .type(Card.Type.PERSONAL)
                .firstName("first")
                .lastName("last")
                .isActive(true)
                .validTill(Instant.parse("2022-02-10T07:04:38.722808400Z"))
                .currency("100")
                .build();

        cardStatus = CardStatus.builder()
                .card(card)
                .status("987")
                .build();

        getCardResponse = GetCardResponse.builder()
                .id(1L)
                .type(GetCardResponse.Type.PERSONAL)
                .validTill(Instant.parse("2022-02-10T07:04:38.722808400Z"))
                .firstName("first")
                .lastName("last")
                .currentStatus("987")
                .currency("100")
                .isActive(true)
                .build();

    }

    @Test
    public void testGetStatus() {

        lenient().when(cardMapperService.getCurrency(Mockito.any(String.class))).thenReturn("100");
        lenient().when(cardMapperService.getStatus(Mockito.any(String.class))).thenReturn("987");

        assertEquals(getCardResponse, converterService.cardStatusToGetCardResponse(cardStatus));
    }
}
