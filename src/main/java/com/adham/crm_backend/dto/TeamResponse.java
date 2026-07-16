package com.adham.crm_backend.dto;

import java.time.Instant;

public record TeamResponse(Long id, String name, Long managerId, String managerName, Instant createdAt) {
}
