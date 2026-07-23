package com.adham.crm_backend.exception;

public class MissingOwnerException extends RuntimeException {
    public MissingOwnerException(String message) {
        super(message);
    }
}
