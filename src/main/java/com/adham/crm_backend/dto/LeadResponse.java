package com.adham.crm_backend.dto;

import com.adham.crm_backend.entity.LeadSource;
import com.adham.crm_backend.entity.LeadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for lead information")
public class LeadResponse {
    @Schema(description = "Unique identifier of the lead")
    private Long id;

    @Schema(description = "Full name of the lead", example = "John Doe")
    private String fullName;

    @Schema(description = "Email address of the lead", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the lead", example = "+1234567890")
    private String phone;

    @Schema(description = "Company name", example = "Acme Corporation")
    private String companyName;

    @Schema(description = "Current status of the Lead")
    private LeadStatus status;

    @Schema(description = "source of the Lead")
    private LeadSource source;

    @Schema(description = "ID of the owner assigned to this lead")
    private Long ownerId;

    @Schema(description = "Name of the owner assigned to this lead")
    private String ownerName;

    @Schema(description = "ID of the user who created this lead record")
    private Long createdById;

    @Schema(description = "Name of the user who created this lead record")
    private String createdByName;

    @Schema(description = "Timestamp when the lead record was created")
    private Instant createdAt;

    @Schema(description = "Timestamp when the lead record was last updated")
    private Instant updatedAt;
}
