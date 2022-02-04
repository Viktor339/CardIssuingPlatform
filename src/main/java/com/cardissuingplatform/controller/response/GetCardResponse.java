package com.cardissuingplatform.controller.response;

import com.cardissuingplatform.model.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Date;


@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class GetCardResponse {

    private Long id;
    private Card.Type type;
    private Instant validTill;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String currentStatus;
    private String currency;

}
