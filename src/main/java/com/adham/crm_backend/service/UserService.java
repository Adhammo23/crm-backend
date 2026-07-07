package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.UserResponse;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.mapper.UserMapper;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getCurrentUser(){

        UserDetails userDetails = SecurityUtils.getCurrentUser();

        User user = userRepository.findByEmailWithRoles(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found in database"
                ));
        return userMapper.toResponse(user);
    }

}
