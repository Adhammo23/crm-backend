package com.adham.crm_backend.exception;

public class ManagerAlreadyAssignedException extends BusinessConflictException {
    public ManagerAlreadyAssignedException(String message) {
        super(message);
    }
}
