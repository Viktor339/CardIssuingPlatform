package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.service.CardMapperService;
import com.cardissuingplatform.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardMapperService cardMapperService;


    private GetCardDto getCardDto;
    private final List<GetCardResponse> list = new ArrayList<>();

    @BeforeEach
    public void init() {
        getCardDto = GetCardDto.builder()
                .currency("978")
                .page(0)
                .size(1)
                .build();

        list.add(GetCardResponse.builder()
                .id(2L)
                .type(GetCardResponse.Type.PERSONAL)
                .validTill(Instant.parse("2022-02-03T21:00:00Z"))
                .firstName("Bready")
                .lastName("Pitt")
                .isActive(true)
                .currency("Евро")
                .build());
    }

    @Test
    public void test() {

        when(cardMapperService.getCurrency("978")).thenReturn("Евро");

        assertEquals(list, cardService.get(getCardDto));
    }
}
