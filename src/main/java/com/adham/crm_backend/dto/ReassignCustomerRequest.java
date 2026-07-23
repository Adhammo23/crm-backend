package com.adham.crm_backend.dto;

import jakarta.validation.constraints.NotNull;

public record ReassignCustomerRequest(@NotNull
                                      Long ownerId) {
}
