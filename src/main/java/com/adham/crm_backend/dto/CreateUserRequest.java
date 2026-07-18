package com.adham.crm_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @Schema(description = "Full name", example = "John Smith")
    @NotBlank
    @Size(min = 2,max = 100)
    private String fullName;

    @Schema(description = "Email address", example = "john.smith@crm.com")
    @Email
    @NotBlank(message = "email is required")
    private String email;

    @Schema(description = "Password", example = "User12345")
    @NotBlank(message = "password is required")
    @Size(min = 8)
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d).+$",
            message = " requiring at least one letter and one digit")
    private String password;

    @Schema(description = "Role IDs assigned to the user", example = "[1, 2]")
    @NotEmpty(message = "At least one role is required.")
    private Set<Long> roleIds;

    @Schema(description = "Optional team ID", example = "3")
    private Long teamId;
}
