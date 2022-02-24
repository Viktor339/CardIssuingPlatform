package com.cardissuingplatform.service.exception;

public class UserNotBelongToTheCompanyException extends RuntimeException {
    public UserNotBelongToTheCompanyException(String message) {
        super(message);
    }

}
