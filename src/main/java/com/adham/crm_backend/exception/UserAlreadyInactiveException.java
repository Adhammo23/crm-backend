package com.adham.crm_backend.exception;

public class UserAlreadyInactiveException extends BusinessConflictException{
    public UserAlreadyInactiveException(String message){super(message);}
}
