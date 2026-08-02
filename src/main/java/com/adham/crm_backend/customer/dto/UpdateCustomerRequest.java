package com.adham.crm_backend.customer.dto;

import jakarta.validation.constraints.Email;

public record UpdateCustomerRequest(String fullName,
                                    @Email String email,
                                    String phone,
                                    String companyName,
                                    String jobTitle) {
}
