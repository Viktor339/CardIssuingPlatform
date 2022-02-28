package com.cardissuingplatform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessageBrokerConfig {

    private final MessageBrokerProperties messageBrokerProperties;

    @Bean("connectionFactory")
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(messageBrokerProperties.getHost(), messageBrokerProperties.getPort());
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean("rabbitTemplate")
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;

    }

    @Bean(MessageBrokerProperties.CARD_REQUEST_QUEUE)
    public Queue queue() {
        return new Queue(MessageBrokerProperties.CARD_REQUEST_QUEUE);
    }

    @Bean(MessageBrokerProperties.CARD_STATUS_QUEUE)
    public Queue cardStatusQueue() {
        return new Queue(MessageBrokerProperties.CARD_STATUS_QUEUE);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule()));
    }

}
