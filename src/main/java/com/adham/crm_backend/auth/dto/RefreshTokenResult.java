package com.adham.crm_backend.auth.dto;

import com.adham.crm_backend.auth.RefreshToken;

public record RefreshTokenResult(String rawToken,
                                 RefreshToken refreshToken) {
}
