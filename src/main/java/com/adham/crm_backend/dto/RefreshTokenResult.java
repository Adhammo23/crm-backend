package com.adham.crm_backend.dto;

import com.adham.crm_backend.entity.RefreshToken;

public record RefreshTokenResult(String rawToken,
                                 RefreshToken refreshToken) {
}
