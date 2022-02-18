package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.controller.request.ChangeAuthorityRequest;
import com.cardissuingplatform.controller.response.ChangeAuthorityResponse;
import com.cardissuingplatform.controller.response.GetAuthorityResponse;
import com.cardissuingplatform.functional.IntegrationTestBase;
import com.cardissuingplatform.functional.TestUtil;
import com.cardissuingplatform.service.exception.UserNotBelongToTheCompanyException;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.cardissuingplatform.functional.TestUtil.objectToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorityControllerTest extends IntegrationTestBase {

    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2NvdW50YW50Iiwicm9sZXMiOlsiUk9MRV9BQ0NPVU5UQU5UIl0sInVzZXJJZCI6NCwiY29tcGFueUlkIjp7ImlkIjoxLCJuYW1lIjoiQSIsImVuYWJsZWQiOnRydWV9LCJpYXQiOjE2NDUxMDU0NjEsImV4cCI6ODgwNDUwMTkwNjF9.DUq3CalRCLT5XoK4lh_AasWdCnfE63Q_jRI8_NzCRpc";

    @Autowired
    private MockMvc mockMvc;

    private ChangeAuthorityRequest changeAuthorityRequest;
    private ChangeAuthorityResponse changeAuthorityResponse;
    private GetAuthorityResponse getAuthorityResponse;

    @BeforeEach
    void setUp() {
        changeAuthorityRequest = new ChangeAuthorityRequest();
        changeAuthorityRequest.setUserId(2L);
        changeAuthorityRequest.setAuthorities(List.of(ChangeAuthorityRequest.Authority.READ_CARD_HISTORY, ChangeAuthorityRequest.Authority.READ_CORPORATE_CARDS));

        List<String> authorities = List.of("READ_CARD_HISTORY", "READ_CORPORATE_CARDS");

        changeAuthorityResponse = ChangeAuthorityResponse.builder()
                .userId(2L)
                .authorities(authorities)
                .build();

        getAuthorityResponse = GetAuthorityResponse.builder()
                .userId(2L)
                .authorities(authorities)
                .build();
    }

    @Test
    void change() throws Exception {

        String contentAsString = mockMvc.perform(put("/accountant/v1/users/authorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(changeAuthorityRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = TestUtil.getObjectMapper();
        ChangeAuthorityResponse changeAuthorityResponse = objectMapper.readValue(contentAsString, ChangeAuthorityResponse.class);

        assertEquals(changeAuthorityResponse, this.changeAuthorityResponse);

    }

    @Test
    void changeShouldThrowUserNotFoundException() throws Exception {

        changeAuthorityRequest.setUserId(9999L);

        mockMvc.perform(put("/accountant/v1/users/authorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(changeAuthorityRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));

    }

    @Test
    void changeShouldThrowUserNotBelongToTheCompanyException() throws Exception {

        changeAuthorityRequest.setUserId(1L);

        mockMvc.perform(put("/accountant/v1/users/authorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(changeAuthorityRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotBelongToTheCompanyException));

    }


    @Test
    void get() throws Exception {

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/accountant/v1/users/authorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "1")
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = TestUtil.getObjectMapper();

        GetAuthorityResponse expect = List.of(objectMapper.readValue(contentAsString, GetAuthorityResponse[].class)).get(0);

        assertEquals(this.getAuthorityResponse, expect);
    }
}