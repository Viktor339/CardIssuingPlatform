package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.controller.dto.ChangeCompanyResponse;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.controller.response.GetCompanyResponse;
import com.cardissuingplatform.functional.IntegrationTestBase;
import com.cardissuingplatform.service.exception.CompanyAlreadyExistsException;
import com.cardissuingplatform.service.exception.CompanyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.cardissuingplatform.functional.TestUtil.objectToJson;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanyControllerTest extends IntegrationTestBase {

    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwidXNlcklkIjozLCJjb21wYW55SWQiOnsiaWQiOjEsIm5hbWUiOiJBIiwiZW5hYmxlZCI6dHJ1ZX0sImlhdCI6MTY0NTA5MTI4OSwiZXhwIjo4ODA0NTAwNDg4OX0.l3fBwfC30b1Ih000Ie_p4PB4dSHsOGGyDC3h-GHw458";

    @Autowired
    private MockMvc mockMvc;

    private CreateCompanyRequest createCompanyRequest;
    private ChangeCompanyRequest changeCompanyRequest;
    private ChangeCompanyResponse changeCompanyResponse;
    private GetCompanyResponse getCompanyResponse;

    @BeforeEach
    public void setUp() {
        createCompanyRequest = new CreateCompanyRequest();
        createCompanyRequest.setName("A");
        createCompanyRequest.setIsEnabled(true);

        changeCompanyRequest = new ChangeCompanyRequest();
        changeCompanyRequest.setName("A");
        changeCompanyRequest.setIsEnabled(true);

        changeCompanyResponse = ChangeCompanyResponse.builder()
                .id(1L)
                .name("A")
                .isEnabled(true)
                .build();

        getCompanyResponse = GetCompanyResponse.builder()
                .id(1L)
                .name("A")
                .isEnabled(true)
                .build();

    }

    @Test
    public void testCreateShouldThrowCompanyAlreadyExistsException() throws Exception {

        mockMvc.perform(post("/accountant/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(createCompanyRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CompanyAlreadyExistsException));
    }

    @Test
    public void testCreateShouldReturnId() throws Exception {

        createCompanyRequest.setName("F");

        mockMvc.perform(post("/accountant/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(createCompanyRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testChangeCompanyNotFoundException() throws Exception {

        changeCompanyRequest.setName("D");

        mockMvc.perform(patch("/accountant/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(changeCompanyRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CompanyNotFoundException));
    }


    @Test
    public void testChangeCompanyShouldReturnChangeCompanyDto() throws Exception {

        mockMvc.perform(patch("/accountant/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(changeCompanyRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectToJson(changeCompanyResponse))));
    }


    @Test
    public void testGetShouldReturnPage() throws Exception {

        mockMvc.perform(get("/accountant/v1/companies")
                        .param("page", "0")
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectToJson(getCompanyResponse))));
    }
}

