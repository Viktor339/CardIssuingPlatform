package com.cardissuingplatform.service;

import org.springframework.stereotype.Component;

@Component
public class PageService {

    public Integer validatePageSize(Integer page, Integer configMinPageSize, Integer configMaxPageSize) {

        if (page < configMinPageSize) {
            page = configMinPageSize;
        }

        if (page > configMaxPageSize) {
            page = configMaxPageSize;
        }

        return page;
    }

}
