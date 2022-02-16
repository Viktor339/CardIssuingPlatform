package com.cardissuingplatform.unit;

import com.cardissuingplatform.repository.specification.CardSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CardSpecificationTest {

    private String currency;
    private String status;
    private Boolean isActive;


    @Test
    void withCurrencyShouldReturnNull() {
        assertNull(CardSpecification.withCurrency(currency));
    }

    @Test
    void withCurrencyShouldReturnNotNull() {
        currency = "curr";
        assertNotNull(CardSpecification.withCurrency(currency));
    }


    @Test
    void withActiveShouldReturnNull() {
        assertNull(CardSpecification.withActive(isActive));
    }

    @Test
    void withActiveShouldReturnNotNull() {
        assertNotNull(CardSpecification.withActive(true));
    }

    @Test
    void withCardStatusShouldReturnNull() {
        assertNull(CardSpecification.withCardStatus(status));
    }

    @Test
    void withCardStatusShouldReturnNotNull() {
        status = "status";
        assertNotNull(CardSpecification.withCardStatus(status));
    }
}