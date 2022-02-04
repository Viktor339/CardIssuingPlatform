package com.cardissuingplatform.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "code.currency")
public class CardProperties {
    @NotNull
    private String eur;
    @NotNull
    private String jpy;
    @NotNull
    private String gbp;
}
