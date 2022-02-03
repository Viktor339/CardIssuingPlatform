package com.cardissuingplatform.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class GetCompanyResponse {
    private Long id;
    private String name;
    private Boolean isEnabled;
}
