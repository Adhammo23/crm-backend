package com.adham.crm_backend.lead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Lead l SET l.status = :newStatus, l.convertedCustomerId = :customerId WHERE l.id = :id AND l.status = :oldStatus")
    int updateStatusIfMatches(@Param("id") Long id,
                              @Param("oldStatus") LeadStatus oldStatus,
                              @Param("newStatus") LeadStatus newStatus,
                              @Param("customerId") Long customerId);
}
