package com.cardissuingplatform.service;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    List<T> items;

    public Page(org.springframework.data.domain.Page<T> page) {
        this.items = page.getContent();
    }
}
