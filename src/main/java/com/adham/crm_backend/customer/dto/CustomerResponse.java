package com.adham.crm_backend.customer.dto;

import com.adham.crm_backend.customer.CustomerStatus;
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
@Schema(description = "Response DTO for customer information")
public class CustomerResponse {
    @Schema(description = "Unique identifier of the customer")
    private Long id;

    @Schema(description = "Full name of the customer", example = "John Doe")
    private String fullName;

    @Schema(description = "Email address of the customer", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the customer", example = "+1234567890")
    private String phone;

    @Schema(description = "Company name", example = "Acme Corporation")
    private String companyName;

    @Schema(description = "Current status of the customer")
    private CustomerStatus status;

    @Schema(description = "ID of the owner assigned to this customer")
    private Long ownerId;

    @Schema(description = "Name of the owner assigned to this customer")
    private String ownerName;

    @Schema(description = "ID of the user who created this customer record")
    private Long createdById;

    @Schema(description = "Name of the user who created this customer record")
    private String createdByName;

    @Schema(description = "Timestamp when the customer record was created")
    private Instant createdAt;

    @Schema(description = "Timestamp when the customer record was last updated")
    private Instant updatedAt;
}
