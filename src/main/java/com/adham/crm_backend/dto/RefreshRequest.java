package com.adham.crm_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest( @NotBlank
                               String refreshToken) {

}
