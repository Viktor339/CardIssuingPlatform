package com.cardissuingplatform.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(toBuilder = true)
@Data
public class TokenDto {

    private List<String> roles;
    private Long userId;
}
