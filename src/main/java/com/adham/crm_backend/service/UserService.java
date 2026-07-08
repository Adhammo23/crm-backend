package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.CreateUserRequest;
import com.adham.crm_backend.dto.UserResponse;
import com.adham.crm_backend.entity.Role;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.exception.InvalidRoleIdException;
import com.adham.crm_backend.exception.UserAlreadyExistsException;
import com.adham.crm_backend.mapper.UserMapper;
import com.adham.crm_backend.repository.RoleRepository;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
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

}
