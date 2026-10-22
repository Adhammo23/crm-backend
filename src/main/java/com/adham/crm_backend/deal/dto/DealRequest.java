package com.adham.crm_backend.deal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "customer is required")
    private Long customerId;

    @NotNull(message = "owner is required")
    private Long ownerId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    private LocalDate expectedCloseDate;
}
