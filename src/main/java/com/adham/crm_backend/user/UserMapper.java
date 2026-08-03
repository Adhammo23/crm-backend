package com.adham.crm_backend.user;
import com.adham.crm_backend.user.dto.UserResponse;
import com.adham.crm_backend.user.entity.Role;
import com.adham.crm_backend.team.Team;
import com.adham.crm_backend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "active", target = "active")
    @Mapping(target = "teamName",source = "team", qualifiedByName = "teamToName")
    UserResponse toResponse(User user);

    default Set<String> mapRoles(Set<Role> roles){
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
    @Named("teamToName")
    default String teamToName(Team team){
        return team != null ? team.getName():null;
    }
}
