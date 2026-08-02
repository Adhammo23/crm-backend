package com.adham.crm_backend.team;

import com.adham.crm_backend.common.exception.BusinessConflictException;
import com.adham.crm_backend.team.exception.InvalidTeamManagerException;
import com.adham.crm_backend.team.exception.ManagerAlreadyAssignedException;
import com.adham.crm_backend.common.exception.ResourceNotFoundException;
import com.adham.crm_backend.team.dto.AssignManagerRequest;
import com.adham.crm_backend.team.dto.CreateTeamRequest;
import com.adham.crm_backend.team.dto.TeamResponse;
import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.entity.User;
import com.adham.crm_backend.user.repository.UserRepository;
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

        Team team = Team.builder()
                .name(request.name())
                .build();

        User manager = null;

        if (request.managerId() != null) {
            manager = findUserById(request.managerId());

            if (!manager.hasRole(RoleName.ROLE_MANAGER)) {
                throw new InvalidTeamManagerException(
                        "User must have ROLE_MANAGER."
                );
            }

            if (manager.getTeam() != null) {
                throw new ManagerAlreadyAssignedException(
                        "User is already assigned to a team."
                );
            }

            team.setManager(manager);
        }

        teamRepository.save(team);

        if (manager != null) {
            manager.setTeam(team);
        }

        return teamMapper.toResponse(team);
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
}