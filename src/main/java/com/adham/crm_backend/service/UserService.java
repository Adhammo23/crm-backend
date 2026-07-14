package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.CreateUserRequest;
import com.adham.crm_backend.dto.UpdateUserRequest;
import com.adham.crm_backend.dto.UserResponse;
import com.adham.crm_backend.entity.Role;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.exception.InvalidRoleIdException;
import com.adham.crm_backend.exception.ResourceNotFoundException;
import com.adham.crm_backend.exception.UserAlreadyExistsException;
import com.adham.crm_backend.mapper.UserMapper;
import com.adham.crm_backend.repository.RoleRepository;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserResponse getCurrentUser(){

        UserDetails userDetails = SecurityUtils.getCurrentUser();

        User user = userRepository.findByEmailWithRoles(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found in database"
                ));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email " + request.getEmail());
        }

        List<Role> roles = roleRepository.findAllById(request.getRoleIds());

        if (request.getRoleIds().size() != roles.size()){
                throw new InvalidRoleIdException("One or more provided role IDs do not exist.");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(roles))
                .build();

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    public UserResponse getUserById(Long id){

        UserDetails currentUser = SecurityUtils.getCurrentUser();

       User targetUser = findUserById(id);

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !currentUser.getUsername().equals(targetUser.getEmail())) {
            throw new AccessDeniedException("You are not allowed to access this user.");
        }
        return userMapper.toResponse(targetUser);
    }


    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request){
        User user = findUserById(id);
        user.setFullName(request.fullName());

        if (!user.getEmail().equals(request.email())){
            if (userRepository.existsByEmail(request.email())){
                throw new
                        UserAlreadyExistsException("Email already exist with email:"+request.email());
            }
            user.setEmail(request.email());
        }

        List<Role> roles = roleRepository.findAllById(request.roleIds());
        if (request.roleIds().size() != roles.size()){
            throw new InvalidRoleIdException("One or more provided role IDs do not exist.");
        }
        user.setRoles(new HashSet<>(roles));

        return userMapper.toResponse(user);
    }
    @Transactional
    public UserResponse activateUser(Long id) {

        User user = findUserById(id);

        changeUserActiveStatus(user, true);

        return userMapper.toResponse(user);
    }

    private User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with Id: "+id));
    }
    private void changeUserActiveStatus(User user, boolean status){
        if (user.isActive() == status) {

            if (status) {
                throw new UserAlreadyActiveException("User is already active.");
            }

            throw new UserAlreadyInactiveException("User is already inactive.");
        }

        user.setActive(status);
    }


}
