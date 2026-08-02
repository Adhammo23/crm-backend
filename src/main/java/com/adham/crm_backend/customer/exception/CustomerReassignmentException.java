package com.adham.crm_backend.customer.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class CustomerReassignmentException extends BusinessConflictException {
    public CustomerReassignmentException(String message) {
        super(message);
    }
}
