package com.cardissuingplatform.controller.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @Min(1)
    private Long userId;
    @NotBlank
    private String refreshToken;
}
