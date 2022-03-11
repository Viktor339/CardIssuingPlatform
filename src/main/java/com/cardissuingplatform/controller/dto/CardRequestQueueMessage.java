package com.cardissuingplatform.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
public class CardRequestQueueMessage implements Serializable {

    private Long id;
    private Long number;
    private String firstName;
    private String lastName;
    private Long currency;
}
