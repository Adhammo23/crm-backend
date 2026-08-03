package com.adham.crm_backend.user.dto;

import jakarta.validation.constraints.NotNull;

public record AssignTeamRequest (@NotNull Long teamId){
}
