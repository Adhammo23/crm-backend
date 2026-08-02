package com.adham.crm_backend.customer.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class CustomerAlreadyExistsException extends BusinessConflictException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
