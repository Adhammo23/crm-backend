package com.adham.crm_backend.customer.dto;

import jakarta.validation.constraints.NotNull;

public record ReassignCustomerRequest(@NotNull
                                      Long ownerId) {
}
