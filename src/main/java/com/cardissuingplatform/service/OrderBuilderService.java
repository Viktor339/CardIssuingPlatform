package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.model.Card_;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderBuilderService {
    
    public List<Sort.Order> buildCardServiceOrder(GetCardDto getCardDto) {

        List<Sort.Order> orders = new ArrayList<>();

        if (getCardDto.getSortLastName() != null) {
            if (getCardDto.getSortLastNameBy().equals("desc")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, Card_.LAST_NAME));
            }
            orders.add(new Sort.Order(Sort.Direction.ASC, Card_.LAST_NAME));
        }

        if (getCardDto.getSortName() != null) {
            if (getCardDto.getSortNameBy().equals("desc")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, Card_.FIRST_NAME));
            }
            orders.add(new Sort.Order(Sort.Direction.ASC, Card_.FIRST_NAME));
        }
        return orders;
    }
}
