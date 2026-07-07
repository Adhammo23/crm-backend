package com.adham.crm_backend.config;

import com.adham.crm_backend.entity.Role;
import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.repository.RoleRepository;
import com.adham.crm_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args){

        if (userRepository.findByEmail("admin@crm.com").isPresent()) return;

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

        User admin = User.builder()
                .fullName("admin")
                .email("admin@crm.com")
                .passwordHash(passwordEncoder.encode("123456"))
                .isActive(true)
                .roles(Set.of(adminRole))
                .build();
        userRepository.save(admin);
    }
}
