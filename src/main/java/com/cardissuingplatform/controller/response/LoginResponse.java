package com.cardissuingplatform.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
}