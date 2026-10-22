package com.adham.crm_backend.deal;

import com.adham.crm_backend.deal.dto.DealResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  DealMapper {
    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "owner.fullName", target = "ownerName")
    DealResponse response(Deal deal);
}
