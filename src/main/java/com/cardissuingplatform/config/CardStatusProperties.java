package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "code.status")
public class CardStatusProperties {
    @NotNull
    private String sentForProcessing;
    @NotNull
    private String waitingForTheStartOfProduction;
    @NotNull
    private String inTheProcessOfManufacturing;
    @NotNull
    private String waitingForDepartureToTheBranch;
    @NotNull
    private String onDepartment;
    @NotNull
    private String transferredToTheClient;
}
