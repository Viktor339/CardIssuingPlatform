package com.cardissuingplatform.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ChangeAuthorityResponse {
    private Long userId;
    private List<String> authorities;
}
