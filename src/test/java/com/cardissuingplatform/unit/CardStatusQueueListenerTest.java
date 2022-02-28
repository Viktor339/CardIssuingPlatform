package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.CardStatusRequestQueueMessage;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.repository.CardRepository;
import com.cardissuingplatform.repository.CardStatusRepository;
import com.cardissuingplatform.service.exception.CardNotFoundException;
import com.cardissuingplatform.service.listener.CardStatusQueueListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardStatusQueueListenerTest {

    public static final String DATE = "1999-12-12";

    @InjectMocks
    private CardStatusQueueListener cardStatusQueueListener;

    @Mock
    private CardStatusRepository cardStatusRepository;
    @Mock
    private CardRepository cardRepository;

    private CardStatusRequestQueueMessage cardStatusRequestQueueMessage;
    private Card card;

    private List<CardStatus> cardStatuses;


    @BeforeEach
    public void init() {
        cardStatusRequestQueueMessage = CardStatusRequestQueueMessage.builder()
                .id(1L)
                .created(Instant.now())
                .status("200")
                .build();

        card = Card.builder()
                .id(1L)
                .build();
        LocalDate localDate = LocalDate.parse(DATE);

        cardStatuses = List.of(CardStatus.builder()
                .card(card)
                .created(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                .id(1L)
                .build());
    }

    @Test
    void testProcessQueueShouldSaveNewCardStatus() {

        when(cardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(cardStatusRepository.findAllCardStatusByCardId(any(Long.class))).thenReturn(cardStatuses);

        cardStatusQueueListener.processQueue(cardStatusRequestQueueMessage);

        verify(cardRepository, times(1)).findById(argThat(id -> id.equals(1L)));
        verify(cardStatusRepository, times(1)).findAllCardStatusByCardId(argThat(id -> id.equals(1L)));
        verify(cardStatusRepository, times(1)).save(argThat(status -> status.getStatus().equals("200")));

    }

    @Test
    void testProcessQueueShouldThrowCardNotFoundException() {

        when(cardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardStatusQueueListener.processQueue(cardStatusRequestQueueMessage));

        verify(cardRepository, times(1)).findById(argThat(id -> id.equals(1L)));

    }


}