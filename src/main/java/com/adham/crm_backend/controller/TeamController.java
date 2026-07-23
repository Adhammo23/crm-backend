package com.adham.crm_backend.controller;

import com.adham.crm_backend.documentation.annotation.CreateApiResponses;
import com.adham.crm_backend.documentation.annotation.GetApiResponses;
import com.adham.crm_backend.documentation.annotation.UpdateApiResponses;
import com.adham.crm_backend.dto.AssignManagerRequest;
import com.adham.crm_backend.dto.CreateTeamRequest;
import com.adham.crm_backend.dto.TeamResponse;
import com.adham.crm_backend.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Team management endpoints")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create team", description = "Create a new team and optionally assign a manager")
    @CreateApiResponses
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "List teams", description = "Return a paginated list of teams for admin users")
    @GetApiResponses
    public ResponseEntity<Page<TeamResponse>> getAllTeams(
            @ParameterObject @PageableDefault(sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(teamService.getAllTeams(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get team by id", description = "Return a single team by id")
    @GetApiResponses
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping("/by-manager/{managerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get team by manager", description = "Return the team assigned to a manager")
    @GetApiResponses
    public ResponseEntity<TeamResponse> getTeamByManagerId(@PathVariable Long managerId) {
        return ResponseEntity.ok(teamService.getTeamByManagerId(managerId));
    }

    @PatchMapping("/{teamId}/manager")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Assign manager to team")
    @UpdateApiResponses
    public ResponseEntity<TeamResponse> assignManager(@PathVariable Long teamId, @Valid @RequestBody AssignManagerRequest request){
        return ResponseEntity.ok(teamService.assignManager(teamId,request));

    }
}