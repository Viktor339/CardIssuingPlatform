package com.cardissuingplatform.service;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private List<T> items;

    public Page(List<T> list) {
        this.items = list;
    }
}
