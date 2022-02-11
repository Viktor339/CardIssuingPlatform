package com.cardissuingplatform.service.exception;

public class UserNotBelongToTheCompany extends RuntimeException{
    public UserNotBelongToTheCompany(String message) {
        super(message);
    }

}
