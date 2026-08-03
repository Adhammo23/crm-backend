package com.adham.crm_backend.auth;

import com.adham.crm_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String hashedToken);
    List<RefreshToken> findAllByUser(User user);
}