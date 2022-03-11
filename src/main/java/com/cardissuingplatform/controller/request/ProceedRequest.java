package com.cardissuingplatform.controller.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class ProceedRequest {

    @NotBlank
    private Type type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate validTill;
    @Positive
    private Long number;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Positive
    private Long currency;
    @Positive
    private Long ownedBy;

    public enum Type {
        PERSONAL, CORPORATE
    }
}
