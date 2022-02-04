package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.repository.CardRepository;
import com.cardissuingplatform.service.CardMapperService;
import com.cardissuingplatform.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private CardMapperService cardMapperService;


    private GetCardDto getCardDto;
    private final List<GetCardResponse> list = new ArrayList<>();

    @BeforeEach
    public void init() {
        getCardDto = GetCardDto.builder()
                .currency("978").build();

        list.add(GetCardResponse.builder()
                .currency("978")
                .currentStatus("100")
                .build());
    }

    @Test
    public void test() {

        when(cardRepository.findByGetCardDto(getCardDto)).thenReturn(list);
        when(cardMapperService.getCurrency("978")).thenReturn("Евро");

        assertEquals(list, cardService.get(getCardDto));
    }
}
