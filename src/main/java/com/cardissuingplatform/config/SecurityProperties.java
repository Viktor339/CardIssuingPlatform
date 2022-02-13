package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "jwt.token")
public class SecurityProperties {

    @NotEmpty
    private String secret;
    @NotNull
    private Duration accessExpired;
    @NotNull
    private Duration refreshExpired;
}
