package com.adham.crm_backend.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){super(message);}
}
