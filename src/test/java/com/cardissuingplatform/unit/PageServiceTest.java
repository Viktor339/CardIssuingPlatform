package com.cardissuingplatform.unit;

import com.cardissuingplatform.service.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @InjectMocks
    private PageService pageService;

    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void validatePageSize(int expectedSize, int pageSize, int min, int max) {

        assertEquals(expectedSize, pageService.validatePageSize(pageSize, min, max));
    }


    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of(2, 2, 1, 3),
                Arguments.of(2, 1, 2, 3),
                Arguments.of(3, 4, 2, 3)
        );
    }
}