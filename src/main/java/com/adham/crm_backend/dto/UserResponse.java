package com.adham.crm_backend.dto;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @Schema(description = "User id", example = "1")
    private Long id;
    @Schema(description = "Full name", example = "John Smith")
    private String fullName;
    @Schema(description = "Email address", example = "john.smith@crm.com")
    private String email;
    @Schema(description = "Assigned roles", example = "[ROLE_USER, ROLE_ADMIN]")
    private Set<String> roles;
    @Schema(description = "Whether the account is active", example = "true")
    private Boolean active;
    @Schema(description = "Creation timestamp", example = "2026-07-16T12:00:00Z")
    private Instant createdAt;
    @Schema(description = "Team name", example = "Engineering")
    private String teamName;
}
