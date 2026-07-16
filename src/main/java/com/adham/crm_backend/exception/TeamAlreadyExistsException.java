package com.adham.crm_backend.exception;

public class TeamAlreadyExistsException extends BusinessConflictException {
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
