package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.controller.request.ProceedRequest;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.controller.response.ProceedResponse;
import com.cardissuingplatform.functional.IntegrationTestBase;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTest extends IntegrationTestBase {

    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2NvdW50YW50Iiwicm9sZXMiOlsiUk9MRV9BQ0NPVU5UQU5UIl0sInVzZXJJZCI6NCwiY29tcGFueUlkIjp7ImlkIjoxLCJuYW1lIjoiQSIsImVuYWJsZWQiOnRydWV9LCJpYXQiOjE2NDUwOTE5NzEsImV4cCI6ODgwNDUwMDU1NzF9.icR_YN95S831SXCOcv7MIJBiJc2q1hkXPZUPsiwdnDg";

    @Autowired
    private MockMvc mockMvc;

    private GetCardResponse getCardResponse;
    @Autowired
    private ObjectMapper objectMapper;

    private ProceedResponse proceedResponse;
    private ProceedRequest proceedRequest;

    @BeforeEach
    public void setUp() {
        getCardResponse = GetCardResponse.builder()
                .firstName("Tommy")
                .lastName("Hanks")
                .isActive(true)
                .validTill(Instant.parse("2022-02-03T21:00:00Z"))
                .id(1L).build();
        proceedResponse = ProceedResponse.builder()
                .id(3L)
                .build();

        proceedRequest = new ProceedRequest();
        proceedRequest.setType(ProceedRequest.Type.PERSONAL);
        proceedRequest.setValidTill(LocalDate.now());
        proceedRequest.setNumber(1L);
        proceedRequest.setCurrency(1L);
        proceedRequest.setOwnedBy(1L);
        proceedRequest.setFirstName("fn");
        proceedRequest.setLastName("ln");

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

    @Test
    public void testProceed() throws Exception {

        mockMvc.perform(post("/accountant/v1/cards/proceed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proceedRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(proceedResponse))));


    }


    @Test
    void changeShouldThrowUserNotFoundException() throws Exception {

        proceedRequest.setOwnedBy(999L);

        mockMvc.perform(post("/accountant/v1/cards/proceed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proceedRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));

    }


}
