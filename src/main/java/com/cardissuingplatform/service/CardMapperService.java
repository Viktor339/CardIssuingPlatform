package com.cardissuingplatform.service;

import com.cardissuingplatform.config.CardProperties;
import com.cardissuingplatform.config.CardStatusProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CardMapperService {

    private CardProperties cardProperties;
    private CardStatusProperties cardStatusProperties;


    public String getCurrency(String currency) {
        return cardProperties.getProperties().getOrDefault(currency, cardProperties.getProperties().get("def"));
    }

    public String getStatus(String status) {
        return cardStatusProperties.getProperties().getOrDefault(status, cardProperties.getProperties().get("def"));
    }
}
