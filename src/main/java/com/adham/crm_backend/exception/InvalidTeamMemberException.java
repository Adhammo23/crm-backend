package com.adham.crm_backend.exception;

public class InvalidTeamMemberException extends RuntimeException {
    public InvalidTeamMemberException(String message) {
        super(message);
    }
}
