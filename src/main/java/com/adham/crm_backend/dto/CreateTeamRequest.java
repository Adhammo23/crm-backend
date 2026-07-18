package com.adham.crm_backend.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTeamRequest(
		@Schema(description = "Team name", example = "Engineering")
		@NotBlank(message = "name is required")
		String name,

		@Schema(description = "Manager user id", example = "5")
		Long managerId) {
}
