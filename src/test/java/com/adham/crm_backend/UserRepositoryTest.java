package com.adham.crm_backend;

import com.adham.crm_backend.entity.Role;
import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.repository.RoleRepository;
import com.adham.crm_backend.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;

    @Test
    void whenSavingUserWithRole_thenRoleIsPersistedCorrectly() {
        // Arrange
        Role adminRole = Role.builder()
                .name(RoleName.ROLE_ADMIN).build();
        Role savedRole = roleRepository.save(adminRole);

        Set<Role> roles = new HashSet<>();
        roles.add(savedRole);

        User user = User.builder()
                .fullName("testuser")
                .passwordHash("password")
                .email("adham@test.com")
                .roles(roles)
                .build();

        // Act
        User savedUser = userRepository.save(user);

        Optional<User> retrievedUserOptional = userRepository.findById(savedUser.getId());
        // Assert
        assertThat(retrievedUserOptional).isPresent();

        User foundUser = retrievedUserOptional.get();
        assertThat(foundUser.getRoles()).hasSize(1);
        assertThat(foundUser.getRoles()).extracting(Role::getName).containsExactly(RoleName.ROLE_ADMIN);
    }
}
