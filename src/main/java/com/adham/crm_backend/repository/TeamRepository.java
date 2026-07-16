package com.adham.crm_backend.repository;

import com.adham.crm_backend.entity.Team;
import com.adham.crm_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByManager(User manager);
    boolean existsByName(String name);
}
