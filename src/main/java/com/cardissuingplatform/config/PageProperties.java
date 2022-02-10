package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "validator.company.page")
public class PageProperties {

    @PositiveOrZero
    @NotNull
    private Integer min;
    @PositiveOrZero
    @NotNull
    private Integer max;

}
