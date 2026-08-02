package com.adham.crm_backend.exception;

public class InvalidLeadStatusException extends BusinessConflictException {
    public InvalidLeadStatusException(String message) {
        super(message);
    }
}
