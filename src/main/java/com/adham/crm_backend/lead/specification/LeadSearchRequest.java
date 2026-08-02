package com.adham.crm_backend.lead.specification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeadSearchRequest {
    private String fullName;
    private String email;
    private String jobTitle;
    private String CompanyName;
    private Long ownerId;
    private String source;
}
