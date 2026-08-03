package com.adham.crm_backend.customer;

import com.adham.crm_backend.customer.dto.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.fullName", target = "createdByName")
    CustomerResponse toResponse(Customer customer);
}
