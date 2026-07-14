package com.adham.crm_backend.exception;

public class UserAlreadyActiveException extends RuntimeException{
    public UserAlreadyActiveException(String message){super(message);}
}
