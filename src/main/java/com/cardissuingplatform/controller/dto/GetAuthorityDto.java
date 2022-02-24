package com.cardissuingplatform.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class GetAuthorityDto {
    private Long userId;
    private List<String> authorities;
}
