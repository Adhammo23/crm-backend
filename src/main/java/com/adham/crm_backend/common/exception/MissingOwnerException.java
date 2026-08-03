package com.adham.crm_backend.common.exception;

public class MissingOwnerException extends RuntimeException {
    public MissingOwnerException(String message) {
        super(message);
    }
}
