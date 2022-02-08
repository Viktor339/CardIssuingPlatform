package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "code.status")
public class CardStatusProperties {
    @NotEmpty
    private Map<String, String> properties;
}
