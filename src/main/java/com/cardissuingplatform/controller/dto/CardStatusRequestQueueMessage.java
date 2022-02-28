package com.cardissuingplatform.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CardStatusRequestQueueMessage implements Serializable {
    private Long id;
    private String status;
    private Instant created;

}
