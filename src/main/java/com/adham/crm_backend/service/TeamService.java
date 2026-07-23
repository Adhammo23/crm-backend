package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.AssignManagerRequest;
import com.adham.crm_backend.dto.CreateTeamRequest;
import com.adham.crm_backend.dto.TeamResponse;
import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.Team;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.exception.*;
import com.adham.crm_backend.mapper.TeamMapper;
import com.adham.crm_backend.repository.TeamRepository;
import com.adham.crm_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;

    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request) {

        if (teamRepository.existsByName(request.name())) {
            throw new TeamAlreadyExistsException(
                    "Team already exists with name: " + request.name());
        }

        Team team = Team.builder()
                .name(request.name())
                .build();

        if (request.managerId() != null) {
            User manager = findUserById(request.managerId());
            assertManagerNotAlreadyAssigned(manager);
            team.setManager(manager);
        }

        Team savedTeam = teamRepository.save(team);
        return teamMapper.toResponse(savedTeam);
    }

    public TeamResponse getTeamById(Long id) {
        return teamMapper.toResponse(findTeamById(id));
    }

    public TeamResponse getTeamByManagerId(Long managerId) {
        User manager = findUserById(managerId);

        Team team = teamRepository.findByManager(manager)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No team found for manager with id: " + managerId));

        return teamMapper.toResponse(team);
    }

    public Page<TeamResponse> getAllTeams(Pageable pageable) {
        return teamRepository.findAll(pageable).map(teamMapper::toResponse);
    }
    @Transactional
    public TeamResponse assignManager(Long teamId, AssignManagerRequest request) {

        Team team = findTeamById(teamId);

        if (team.getManager() != null){
            throw new BusinessConflictException("team already have manager");
        }

        User manager = findUserById(request.managerId());

        if (!manager.hasRole(RoleName.ROLE_MANAGER)) {
            throw new InvalidTeamManagerException(
                    "User must have ROLE_MANAGER."
            );
        }
        if (manager.getTeam() !=null){
            throw new ManagerAlreadyAssignedException("User with id" + manager.getId() + " already manages a team.");
        }

        team.setManager(manager);
        manager.setTeam(team);

        return teamMapper.toResponse(team);
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team not found with id: " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
    }

    private void assertManagerNotAlreadyAssigned(User manager) {
        if (teamRepository.findByManager(manager).isPresent()) {
            throw new ManagerAlreadyAssignedException(
                    "User with id " + manager.getId() + " already manages a team.");
        }
    }
}