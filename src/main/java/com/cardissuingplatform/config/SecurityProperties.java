package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "jwt.token")
public class SecurityProperties {

    @NotEmpty
    private String secret;
    @PositiveOrZero
    private long accessExpired;
    @PositiveOrZero
    private long refreshExpired;
}
