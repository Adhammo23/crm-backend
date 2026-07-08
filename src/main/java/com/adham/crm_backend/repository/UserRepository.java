package com.adham.crm_backend.repository;

import com.adham.crm_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByEmail(String email);

     @Query("""
    select distinct u
    from User u
    join fetch u.roles
    where u.email = :email
    """)
     Optional<User> findByEmailWithRoles(String email);

     boolean existsByEmail(String email);
}
