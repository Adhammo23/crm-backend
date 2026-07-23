package com.adham.crm_backend.exception;

public class InvalidCustomerOwnerException extends RuntimeException {
    public InvalidCustomerOwnerException(String message) {
        super(message);
    }
}
