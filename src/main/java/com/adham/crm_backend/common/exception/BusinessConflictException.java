package com.adham.crm_backend.common.exception;

public class BusinessConflictException extends RuntimeException {
    public BusinessConflictException(String message){
        super(message);
    }
}
