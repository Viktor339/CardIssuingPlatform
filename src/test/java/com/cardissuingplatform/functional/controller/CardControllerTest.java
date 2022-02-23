package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.functional.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTest extends IntegrationTestBase {

    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2NvdW50YW50Iiwicm9sZXMiOlsiUk9MRV9BQ0NPVU5UQU5UIl0sInVzZXJJZCI6NCwiY29tcGFueUlkIjp7ImlkIjoxLCJuYW1lIjoiQSIsImVuYWJsZWQiOnRydWV9LCJpYXQiOjE2NDUwOTE5NzEsImV4cCI6ODgwNDUwMDU1NzF9.icR_YN95S831SXCOcv7MIJBiJc2q1hkXPZUPsiwdnDg";

    @Autowired
    private MockMvc mockMvc;

    private GetCardResponse getCardResponse;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        getCardResponse = GetCardResponse.builder()
                .firstName("Tommy")
                .lastName("Hanks")
                .isActive(true)
                .validTill(Instant.parse("2022-02-03T21:00:00Z"))
                .id(1L).build();
    }

    @Test
    public void testGetShouldReturnPage() throws Exception {

        String contentAsString = mockMvc.perform(get("/accountant/v1/cards")
                        .param("isActive", "true")
                        .param("page", "0")
                        .param("status", "100")
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GetCardResponse expect = List.of(objectMapper.readValue(contentAsString, GetCardResponse[].class)).get(0);

        Assertions.assertEquals(expect.getId(), getCardResponse.getId());
        Assertions.assertEquals(expect.getFirstName(), getCardResponse.getFirstName());
        Assertions.assertEquals(expect.getLastName(), getCardResponse.getLastName());
        Assertions.assertEquals(expect.getIsActive(), getCardResponse.getIsActive());
        Assertions.assertEquals(expect.getValidTill(), getCardResponse.getValidTill());
    }

}
