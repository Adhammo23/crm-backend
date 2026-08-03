package com.adham.crm_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshRequest(
        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9...")
        @NotBlank(message = "refresh token is required")
        String refreshToken) {

}
