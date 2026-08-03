package com.adham.crm_backend.user.repository;

import com.adham.crm_backend.user.entity.Role;
import com.adham.crm_backend.user.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
