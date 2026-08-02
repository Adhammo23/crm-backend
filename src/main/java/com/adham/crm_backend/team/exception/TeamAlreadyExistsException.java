package com.adham.crm_backend.team.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class TeamAlreadyExistsException extends BusinessConflictException {
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
