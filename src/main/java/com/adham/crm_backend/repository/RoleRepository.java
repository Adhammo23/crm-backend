package com.adham.crm_backend.repository;

import com.adham.crm_backend.entity.Role;
import com.adham.crm_backend.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
