package com.cardissuingplatform.service;

import com.cardissuingplatform.service.exception.ValidatorPageException;
import org.springframework.stereotype.Component;

@Component
public class PageService {

    public void validatePage(Integer page, Integer configMinPageSize, Integer configMaxPageSize) {
        if (page < configMinPageSize | page > configMaxPageSize) {
            throw new ValidatorPageException("Invalid page size");
        }
    }

}
