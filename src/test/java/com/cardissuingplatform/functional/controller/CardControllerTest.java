package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.functional.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetShouldReturnPage() throws Exception {

        mockMvc.perform(get("/accountant/v1/cards")
                        .param("isActive", "true")
                        .param("page", "0")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1,\"type\":\"PERSONAL\"")))
                .andExpect(content().string(containsString("\"firstName\":\"Tom\",\"lastName\":\"Hanks\"")));
    }

}
