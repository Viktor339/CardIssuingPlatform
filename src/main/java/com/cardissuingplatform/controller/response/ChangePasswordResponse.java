package com.cardissuingplatform.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ChangePasswordResponse {
    private Long userId;
}
