package com.cardissuingplatform.controller;

import com.cardissuingplatform.controller.request.ChangePasswordRequest;
import com.cardissuingplatform.controller.request.LoginRequest;
import com.cardissuingplatform.controller.request.RegistrationRequest;
import com.cardissuingplatform.controller.response.ChangePasswordResponse;
import com.cardissuingplatform.controller.response.LoginResponse;
import com.cardissuingplatform.controller.response.RegistrationResponse;
import com.cardissuingplatform.model.annotation.Token;
import com.cardissuingplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accountant/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }


    @PostMapping("/registration")
    public RegistrationResponse registration(@RequestBody @Validated RegistrationRequest registrationRequest) {
        return RegistrationResponse.builder()
                .userId(userService.registration(registrationRequest))
                .build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    @PatchMapping("/password/change")
    public ChangePasswordResponse changePassword(@RequestBody @Validated ChangePasswordRequest changePasswordRequest, @Token String token) {
        return userService.changePassword(changePasswordRequest,token);
    }
}
