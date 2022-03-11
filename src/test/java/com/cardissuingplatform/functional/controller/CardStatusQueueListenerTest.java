package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.controller.dto.CardStatusRequestQueueMessage;
import com.cardissuingplatform.functional.IntegrationTestBase;
import com.cardissuingplatform.repository.CardStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardStatusQueueListenerTest extends IntegrationTestBase {

    public static final String QUEUE = "card_status_queue";
    public static final String DATE = "1999-12-12";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CardStatusRepository cardStatusRepository;

    private CardStatusRequestQueueMessage cardStatusRequestQueueMessage;
    private LocalDate localDate;

    @BeforeEach
    void setUp() {
        cardStatusRequestQueueMessage = CardStatusRequestQueueMessage.builder()
                .id(1L)
                .created(Instant.now())
                .status("200")
                .build();

        localDate = LocalDate.parse(DATE);
    }

    @Test
    public void testShouldInsertNewCardStatus() {

        rabbitTemplate.convertAndSend(QUEUE, cardStatusRequestQueueMessage);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertEquals(4, cardStatusRepository.findAll().size()));

    }

    @Test
    public void testShouldNotInsertNewCardStatus() {

        Instant previousDate = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        cardStatusRequestQueueMessage.setCreated(previousDate);
        rabbitTemplate.convertAndSend(QUEUE, cardStatusRequestQueueMessage);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertEquals(3, cardStatusRepository.findAll().size()));
    }


}
