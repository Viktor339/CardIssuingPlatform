package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "validator.company.page")
public class PageProperties {

    @Min(0)
    @NotNull
    private Integer min;
    @Min(0)
    @NotNull
    private Integer max;

}
