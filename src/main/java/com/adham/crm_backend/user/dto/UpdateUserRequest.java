package com.adham.crm_backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record UpdateUserRequest(@NotBlank(message = "name is required")
                                @Schema(description = "Full name", example = "John Smith")
                                @Size(min = 2,max = 100)
                                String fullName,

                                @Schema(description = "Email address", example = "john.smith@crm.com")
                                @Email
                                @NotBlank(message = "email is required")
                                String email,

                                @Schema(description = "Role IDs assigned to the user", example = "[1, 2]")
                                @NotEmpty
                                Set<Long> roleIds) {
}
