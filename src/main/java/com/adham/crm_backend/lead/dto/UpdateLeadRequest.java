package com.adham.crm_backend.lead.dto;

import jakarta.validation.constraints.Email;

public record UpdateLeadRequest(String fullName,
                                @Email String email,
                                String phone,
                                String companyName,
                                String jobTitle) {
}
