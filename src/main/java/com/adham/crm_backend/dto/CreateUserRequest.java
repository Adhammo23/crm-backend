package com.adham.crm_backend.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d).+$",
            message = " requiring at least one letter and one digit")
    private String password;

    private Set<Long> roleIds;
}
