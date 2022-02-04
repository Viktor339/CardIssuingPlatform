package com.cardissuingplatform.service;

import com.cardissuingplatform.config.CardProperties;
import com.cardissuingplatform.config.CardStatusProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class CardMapperService {

    private CardProperties cardProperties;
    private CardStatusProperties cardStatusProperties;

    private final Map<String, String> currencyMap = new HashMap<>();
    private final Map<String, String> currencyStatus = new HashMap<>();

    @PostConstruct
    public void init() {
        currencyMap.put(cardProperties.getEur(), "Евро");
        currencyMap.put(cardProperties.getGbp(), "Фунт стерлингов Великобритании");
        currencyMap.put(cardProperties.getJpy(), "Японская йена");

        currencyStatus.put(cardStatusProperties.getSentForProcessing(), "Отправлена на обработку");
        currencyStatus.put(cardStatusProperties.getWaitingForTheStartOfProduction(), "Ожидание начала изготовления");
        currencyStatus.put(cardStatusProperties.getInTheProcessOfManufacturing(), "В процессе изготовления");
        currencyStatus.put(cardStatusProperties.getWaitingForDepartureToTheBranch(), "Ожидает отправку в отделение");
        currencyStatus.put(cardStatusProperties.getOnDepartment(), "В отделении");
        currencyStatus.put(cardStatusProperties.getTransferredToTheClient(), "Передана клиенту");
    }


    public String getCurrency(String currency) {

        String res = currencyMap.get(currency);
        if (res == null) {
            res = "Неизвестная валюта";
        }
        return res;
    }

    public String getStatus(String status) {

        String res = currencyStatus.get(status);
        if (res == null) {
            res = "Состояние неизвестно";
        }
        return res;
    }
}
