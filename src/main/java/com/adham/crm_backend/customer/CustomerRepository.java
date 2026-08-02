package com.adham.crm_backend.customer;

import com.adham.crm_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    boolean existsByEmail(String email);

    @Query("""
        SELECT c FROM Customer c
        WHERE c.owner.team.manager = :manager
           OR c.owner = :manager
        """)
    Page<Customer> findVisibleToManager(@Param("manager") User manager, Pageable pageable);

    Page<Customer> findByOwner(User owner, Pageable pageable);
}
