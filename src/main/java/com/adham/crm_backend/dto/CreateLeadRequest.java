package com.adham.crm_backend.dto;

import com.adham.crm_backend.entity.LeadSource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeadRequest {
    @NotBlank(message = "fullName is required")
    @Size(min = 2, max = 150)
    @Schema(description = "Full name of the lead", example = "John Doe", minLength = 2, maxLength = 150)
    private String fullName;

    @Email(message = "email must be a valid email address")
    @NotBlank(message = "email is required")
    @Schema(description = "Email address of the lead", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the lead", example = "+1234567890")
    private String phone;

    @Schema(description = "Company name", example = "Acme Corporation")
    private String companyName;

    @Schema(description = "Job title of the customer", example = "Manager")
    private String jobTitle;

    @Schema(example = "WEBSITE/REFERRAL/COLD_CALL...")
    private LeadSource leadSource;

    @Schema(description = "ID of the owner assigned to this lead")
    private Long ownerId;
}
