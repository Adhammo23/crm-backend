package com.adham.crm_backend.common.security;

import com.adham.crm_backend.user.entity.User;
import com.adham.crm_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmailWithRoles(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found in database: " + email));

    }
}
