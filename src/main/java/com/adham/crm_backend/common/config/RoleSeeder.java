package com.adham.crm_backend.common.config;

import com.adham.crm_backend.user.entity.Role;
import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {

        for ( RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(roleName)
                        .build());
            }
        }

    }
}
