package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.service.OrderBuilderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderBuilderServiceTest {

    private static GetCardDto getCardDto;
    private static GetCardDto getCardDtoTwo;
    @InjectMocks
    private OrderBuilderService orderBuilderService;

    @BeforeAll
    static void setUp() {
        getCardDto = new GetCardDto();
        getCardDto.setSortLastName("lastName");

        getCardDtoTwo = new GetCardDto();
        getCardDtoTwo.setSortName("name");

    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void buildCardServiceOrder(GetCardDto getCardDto,int expectedSize) {

        assertEquals(expectedSize, orderBuilderService.buildCardServiceOrder(getCardDto).size());

    }


    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of(getCardDto, 1),
                Arguments.of(getCardDtoTwo, 1)
        );
    }
}