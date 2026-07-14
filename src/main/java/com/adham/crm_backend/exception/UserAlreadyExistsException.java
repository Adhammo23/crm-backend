package com.adham.crm_backend.exception;

public class UserAlreadyExistsException extends BusinessConflictException{
    public UserAlreadyExistsException(String message){super(message);}
}
