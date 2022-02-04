package com.cardissuingplatform.controller.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class GetCardDto {

    private String sortName;
    private String sortNameBy;
    private String sortLastName;
    private String sortLastNameBy;
    private String currency;
    private String status;
    private Boolean active;
    private Integer size;
    private Integer page;
}
