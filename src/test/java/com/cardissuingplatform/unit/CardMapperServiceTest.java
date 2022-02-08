package com.cardissuingplatform.unit;

import com.cardissuingplatform.service.CardMapperService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class CardMapperServiceTest {

    @Autowired
    private CardMapperService cardMapperService;

    @ParameterizedTest
    @MethodSource("currencyStream")
    public void testGetCurrency(String expect, String currency) {
        assertEquals(expect, cardMapperService.getCurrency(currency));
    }

    static Stream<Arguments> currencyStream() {
        return Stream.of(
                arguments("Евро", "978"),
                arguments("Японская йена", "392"),
                arguments("Фунт стерлингов Великобритании", "826"),
                arguments("Неизвестная валюта", "111")
        );
    }


    @ParameterizedTest
    @MethodSource("statusStream")
    public void testGetStatus(String expect, String status) {
        assertEquals(expect, cardMapperService.getStatus(status));
    }

    static Stream<Arguments> statusStream() {
        return Stream.of(
                arguments("Отправлена на обработку", "100"),
                arguments("Ожидание начала изготовления", "200"),
                arguments("В процессе изготовления", "300"),
                arguments("Состояние неизвестно", "def")
        );
    }
}
