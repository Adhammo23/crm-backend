package com.adham.crm_backend.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record TeamResponse(
		@Schema(description = "Team id", example = "1")
		Long id,

		@Schema(description = "Team name", example = "Engineering")
		String name,

		@Schema(description = "Manager user id", example = "5")
		Long managerId,

		@Schema(description = "Manager name", example = "Jane Doe")
		String managerName,

		@Schema(description = "Creation timestamp", example = "2026-07-16T12:00:00Z")
		Instant createdAt) {
}
