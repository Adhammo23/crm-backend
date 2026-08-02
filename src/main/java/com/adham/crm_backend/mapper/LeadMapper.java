package com.adham.crm_backend.mapper;

import com.adham.crm_backend.dto.LeadResponse;
import com.adham.crm_backend.entity.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    @Mapping(source = "owner.id",target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.fullName", target = "createdByName")
    LeadResponse toResponse(Lead lead);
}
