package com.adham.crm_backend.team;

import com.adham.crm_backend.team.dto.TeamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "managerId", expression = "java(team.getManager() != null ? team.getManager().getId() : null)")
    @Mapping(target = "managerName", expression = "java(team.getManager() != null ? team.getManager().getFullName() : null)")
    TeamResponse toResponse(Team team);
}
