package com.adham.crm_backend.exception;

public class UserAlreadyInactiveException extends RuntimeException{
    public UserAlreadyInactiveException(String message){super(message);}
}
