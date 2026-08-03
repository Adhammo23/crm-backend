package com.adham.crm_backend.user.exception;

import com.adham.crm_backend.common.exception.BusinessConflictException;

public class UserAlreadyActiveException extends BusinessConflictException {
    public UserAlreadyActiveException(String message){super(message);}
}
