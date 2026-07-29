package com.adham.crm_backend.specification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSearchRequest {
    private String fullName;
    private String email;
    private String jobTitle;
    private String phone;
    private String CompanyName;
    private Long ownerId;
}
