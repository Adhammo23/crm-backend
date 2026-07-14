package com.adham.crm_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Size(min = 2,max = 100)
    private String fullName;

    @Email
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8)
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d).+$",
            message = " requiring at least one letter and one digit")
    private String password;

    @NotEmpty(message = "At least one role is required.")
    private Set<Long> roleIds;
}
