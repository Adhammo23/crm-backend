package com.adham.crm_backend.team.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class ManagerAlreadyAssignedException extends BusinessConflictException {
    public ManagerAlreadyAssignedException(String message) {
        super(message);
    }
}
