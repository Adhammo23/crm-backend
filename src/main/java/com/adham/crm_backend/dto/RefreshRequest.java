package com.adham.crm_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest( @NotBlank(message = "refresh token is required")
                               String refreshToken) {

}
