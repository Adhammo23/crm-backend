package com.adham.crm_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTeamRequest(@NotBlank(message = "name is required") String name, Long managerId) {
}
