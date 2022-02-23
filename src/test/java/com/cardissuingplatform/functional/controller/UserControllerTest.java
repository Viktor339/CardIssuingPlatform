package com.cardissuingplatform.functional.controller;

import com.cardissuingplatform.controller.request.ChangePasswordRequest;
import com.cardissuingplatform.controller.request.LoginRequest;
import com.cardissuingplatform.controller.request.RegistrationRequest;
import com.cardissuingplatform.controller.response.ChangePasswordResponse;
import com.cardissuingplatform.controller.response.LoginResponse;
import com.cardissuingplatform.controller.response.RegistrationResponse;
import com.cardissuingplatform.functional.IntegrationTestBase;
import com.cardissuingplatform.service.exception.AuthenticationException;
import com.cardissuingplatform.service.exception.CompanyNotFoundException;
import com.cardissuingplatform.service.exception.RoleNotFoundException;
import com.cardissuingplatform.service.exception.UserAlreadyExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTestBase {

    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2NvdW50YW50Iiwicm9sZXMiOlsiUk9MRV9BQ0NPVU5UQU5UIl0sInVzZXJJZCI6NCwiY29tcGFueUlkIjp7ImlkIjoxLCJuYW1lIjoiQSIsImVuYWJsZWQiOnRydWV9LCJpYXQiOjE2NDUxMDU0NjEsImV4cCI6ODgwNDUwMTkwNjF9.DUq3CalRCLT5XoK4lh_AasWdCnfE63Q_jRI8_NzCRpc";


    @Autowired
    private MockMvc mockMvc;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private RegistrationRequest registrationRequest;
    private RegistrationResponse registrationResponse;
    private ChangePasswordRequest changePasswordRequest;
    private ChangePasswordResponse changePasswordResponse;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("tom");
        loginRequest.setPassword("tom");

        loginResponse = LoginResponse.builder()
                .id(1L)
                .build();

        registrationRequest = RegistrationRequest.builder()
                .username("username")
                .roleId(2L)
                .companyId(1L)
                .password("pass")
                .dateOfBirth(LocalDate.now())
                .email("email")
                .firstName("first")
                .lastName("last")
                .build();

        registrationResponse = RegistrationResponse.builder()
                .userId(5L)
                .build();

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("accountant");
        changePasswordRequest.setNewPassword("accountant");

        changePasswordResponse = ChangePasswordResponse.builder()
                .userId(4L)
                .build();


    }

    @Test
    void login() throws Exception {

        String contentAsString = mockMvc.perform(post("/accountant/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponse loginResponse = objectMapper.readValue(contentAsString, LoginResponse.class);

        Assertions.assertEquals(this.loginResponse.getId(), loginResponse.getId());

    }


    @Test
    void loginShouldThrowAuthenticationException() throws Exception {

        loginRequest.setEmail("sss");

        mockMvc.perform(post("/accountant/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthenticationException));

    }


    @Test
    void loginShouldThrowAuthenticationExceptionByInvalidPass() throws Exception {


        loginRequest.setPassword("sss");

        mockMvc.perform(post("/accountant/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthenticationException));

    }

    @Test
    void registration() throws Exception {

        String contentAsString = mockMvc.perform(post("/accountant/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest))
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RegistrationResponse registrationResponse = objectMapper.readValue(contentAsString, RegistrationResponse.class);

        Assertions.assertEquals(this.registrationResponse.getUserId(), registrationResponse.getUserId());

    }


    @Test
    void registrationShouldThrowUserAlreadyExistException() throws Exception {

        registrationRequest.setUsername("tom");

        mockMvc.perform(post("/accountant/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest))
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistException));
    }

    @Test
    void registrationShouldThrowCompanyNotFoundException() throws Exception {

        registrationRequest.setUsername("user");
        registrationRequest.setCompanyId(5L);

        mockMvc.perform(post("/accountant/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest))
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CompanyNotFoundException));
    }

    @Test
    void registrationShouldThrowRoleNotFoundException() throws Exception {

        registrationRequest.setUsername("user");
        registrationRequest.setRoleId(5L);

        mockMvc.perform(post("/accountant/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest))
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleNotFoundException));
    }


    @Test
    void changePassword() throws Exception {

        String contentAsString = mockMvc.perform(patch("/accountant/v1/users/password/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
                        .header("Authorization", TOKEN)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ChangePasswordResponse changePasswordResponse = objectMapper.readValue(contentAsString, ChangePasswordResponse.class);

        Assertions.assertEquals(this.changePasswordResponse.getUserId(), changePasswordResponse.getUserId());
    }
}