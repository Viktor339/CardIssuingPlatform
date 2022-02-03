package com.cardissuingplatform.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChangeCompanyRequest {
    @NotEmpty()
    private String name;
    @NotNull()
    private Boolean isEnabled;
}
