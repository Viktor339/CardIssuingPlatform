package com.cardissuingplatform.controller;

import com.cardissuingplatform.controller.request.RefreshTokenRequest;
import com.cardissuingplatform.controller.response.RefreshTokenResponse;
import com.cardissuingplatform.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accountant/v1/users/tokens")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('ACCOUNTANT')")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return tokenService.refresh(refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getUserId());
    }
}
