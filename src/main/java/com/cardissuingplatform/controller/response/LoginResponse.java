package com.cardissuingplatform.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
}