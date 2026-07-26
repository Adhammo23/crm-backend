package com.adham.crm_backend.exception;

public class CustomerReassignmentException extends BusinessConflictException {
    public CustomerReassignmentException(String message) {
        super(message);
    }
}
