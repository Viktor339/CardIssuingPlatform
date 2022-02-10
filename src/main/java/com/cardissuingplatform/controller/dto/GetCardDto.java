package com.cardissuingplatform.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCardDto {

    private String sortName;
    private String sortNameBy = "asc";
    private String sortLastName;
    private String sortLastNameBy = "asc";
    private String currency;
    private String status;
    private Boolean isActive;
    private Integer size = 10;
    private Integer page = 0;

}
