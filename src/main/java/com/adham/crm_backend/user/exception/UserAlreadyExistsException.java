package com.adham.crm_backend.user.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class UserAlreadyExistsException extends BusinessConflictException {
    public UserAlreadyExistsException(String message){super(message);}
}
