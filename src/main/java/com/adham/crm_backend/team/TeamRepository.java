package com.adham.crm_backend.team;

import com.adham.crm_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByManager(User manager);
    boolean existsByName(String name);
}
