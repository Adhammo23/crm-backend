package com.adham.crm_backend.exception;

public class LeadAlreadyExistsException extends BusinessConflictException {
    public LeadAlreadyExistsException(String message) {
        super(message);
    }
}
