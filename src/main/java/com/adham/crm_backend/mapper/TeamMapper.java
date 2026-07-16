package com.adham.crm_backend.mapper;

import com.adham.crm_backend.dto.TeamResponse;
import com.adham.crm_backend.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "managerId", expression = "java(team.getManager() != null ? team.getManager().getId() : null)")
    @Mapping(target = "managerName", expression = "java(team.getManager() != null ? team.getManager().getFullName() : null)")
    TeamResponse toResponse(Team team);
}
