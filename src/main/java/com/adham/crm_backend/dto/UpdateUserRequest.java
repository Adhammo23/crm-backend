package com.adham.crm_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(@NotBlank(message = "name is required")
                                @Size(min = 2,max = 100)
                                String fullName,

                                @Email
                                @NotBlank(message = "email is required")
                                String email,

                                @NotEmpty
                                Set<Long> roleIds) {
}
