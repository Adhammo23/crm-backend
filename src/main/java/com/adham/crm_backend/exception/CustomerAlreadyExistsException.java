package com.adham.crm_backend.exception;

public class CustomerAlreadyExistsException extends BusinessConflictException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
