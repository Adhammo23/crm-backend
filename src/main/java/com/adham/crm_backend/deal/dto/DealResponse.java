package com.adham.crm_backend.deal.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealResponse {
    private Long id;
    private String customerName;
    private String title;
    private BigDecimal amount;
    private String ownerName;
    private String status;
}
