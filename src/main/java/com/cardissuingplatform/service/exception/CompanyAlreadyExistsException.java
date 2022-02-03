package com.cardissuingplatform.service.exception;

public class CompanyAlreadyExistsException extends RuntimeException{
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
