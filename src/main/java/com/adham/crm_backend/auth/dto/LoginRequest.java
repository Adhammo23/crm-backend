package com.adham.crm_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "User email address", example = "admin@crm.com")
        @NotBlank(message = "email is required")
        @Email
        String email,

        @Schema(description = "User password", example = "Admin1234")
        @NotBlank(message = "password is required")
        String password) {
}
