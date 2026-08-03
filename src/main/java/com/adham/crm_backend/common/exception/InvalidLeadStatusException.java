package com.adham.crm_backend.common.exception;

public class InvalidLeadStatusException extends BusinessConflictException {
    public InvalidLeadStatusException(String message) {
        super(message);
    }
}
