package com.cardissuingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Configuration
@Validated
@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class MessageBrokerProperties {

    public static final String CARD_REQUEST_QUEUE = "card_request_queue";
    public static final String CARD_STATUS_QUEUE = "card_status_queue";

    @NotBlank
    private String host;
    @PositiveOrZero
    @NotNull
    private Integer port;
}
