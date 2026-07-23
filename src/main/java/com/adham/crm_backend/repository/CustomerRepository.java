package com.adham.crm_backend.repository;

import com.adham.crm_backend.entity.Customer;
import com.adham.crm_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    @Query("""
        SELECT c FROM Customer c
        WHERE c.owner.team.manager = :manager
           OR c.owner = :manager
        """)
    Page<Customer> findVisibleToManager(@Param("manager") User manager, Pageable pageable);

    Page<Customer> findByOwner(User owner, Pageable pageable);
}
