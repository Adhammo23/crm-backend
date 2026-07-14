package com.adham.crm_backend.exception;

public class UserAlreadyActiveException extends BusinessConflictException{
    public UserAlreadyActiveException(String message){super(message);}
}
