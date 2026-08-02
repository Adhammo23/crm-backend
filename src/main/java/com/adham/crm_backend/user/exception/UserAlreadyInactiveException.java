package com.adham.crm_backend.user.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class UserAlreadyInactiveException extends BusinessConflictException {
    public UserAlreadyInactiveException(String message){super(message);}
}
