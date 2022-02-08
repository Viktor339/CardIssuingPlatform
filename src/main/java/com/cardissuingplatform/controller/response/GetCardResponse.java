package com.cardissuingplatform.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;


@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class GetCardResponse {

    private Long id;
    private Type type;
    private Instant validTill;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String currentStatus;
    private String currency;

    public enum Type {
        PERSONAL, CORPORATE
    }

}
