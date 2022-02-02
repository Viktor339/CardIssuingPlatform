package com.cardissuingplatform.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ChangeCompanyDto {
    private Long id;
    private String name;
    private Boolean isEnabled;
}
