package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.functional.IntegrationTestBase;
import com.cardissuingplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application.yaml", properties = "cache.enabled = false")
public class UserProviderWithoutCacheTest extends IntegrationTestBase {

    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2NvdW50YW50Iiwicm9sZXMiOlsiUk9MRV9BQ0NPVU5UQU5UIl0sInVzZXJJZCI6NCwiY29tcGFueUlkIjp7ImlkIjoxLCJuYW1lIjoiQSIsImVuYWJsZWQiOnRydWV9LCJpYXQiOjE2NDUwOTE5NzEsImV4cCI6ODgwNDUwMDU1NzF9.icR_YN95S831SXCOcv7MIJBiJc2q1hkXPZUPsiwdnDg";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserRepository userRepository;

    @Test
    public void testGetShouldReturnPage() throws Exception {

        mockMvc.perform(get("/accountant/v1/cards")
                        .param("isActive", "true")
                        .param("page", "0")
                        .param("status", "100")
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/accountant/v1/cards")
                        .param("isActive", "true")
                        .param("page", "0")
                        .param("status", "100")
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(userRepository, times(4)).findUserById(Mockito.argThat(create -> create.equals(4L)));

    }
}
