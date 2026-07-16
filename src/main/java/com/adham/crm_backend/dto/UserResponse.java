package com.adham.crm_backend.dto;
import lombok.*;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private Set<String> roles;
    private Boolean active;
    private Instant createdAt;
    private String teamName;
}
