package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.model.Card_;
import com.cardissuingplatform.repository.CardStatusRepository;
import com.cardissuingplatform.service.CardService;
import com.cardissuingplatform.service.ConverterService;
import com.cardissuingplatform.service.OrderBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardStatusRepository cardStatusRepository;

    @Mock
    private ConverterService converterService;

    @Mock
    private OrderBuilderService orderBuilderService;

    @InjectMocks
    private CardService cardService;

    private GetCardResponse getCardResponse;
    private GetCardDto getCardDto;
    private final List<GetCardResponse> list = new ArrayList<>();
    private PageImpl<CardStatus> pagedResponse;
    List<Sort.Order> orders = new ArrayList<>();

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

        orders.add(new Sort.Order(Sort.Direction.ASC, Card_.LAST_NAME));

    }

    @Test
    public void testGet() {

        when(cardStatusRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(pagedResponse);
        when(converterService.cardStatusToGetCardResponse(Mockito.any(CardStatus.class))).thenReturn(getCardResponse);
        when(orderBuilderService.buildCardServiceOrder(getCardDto)).thenReturn(orders);

        assertEquals(list, cardService.get(getCardDto));

        verify(orderBuilderService, times(1)).buildCardServiceOrder(argThat(gDto -> gDto.getStatus().equals("100")));
        verify(cardStatusRepository, times(1)).findAll((Specification<CardStatus>) argThat(Objects::nonNull), (Pageable) argThat(Objects::nonNull));
        verify(converterService, times(1)).cardStatusToGetCardResponse(argThat(cardStatus -> cardStatus.getId() == 1L));

    }
}
