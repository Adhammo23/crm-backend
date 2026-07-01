package com.adham.crm_backend.dto;

public record AuthResponse( String accessToken,
         String refreshToken,
         Long expiresIn) {

}
