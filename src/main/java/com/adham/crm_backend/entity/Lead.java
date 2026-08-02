package com.adham.crm_backend.entity;

import com.adham.crm_backend.security.HasOwner;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "leads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lead implements HasOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_title")
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadSource source;      // WEBSITE/REFERRAL/COLD_CALL/SOCIAL_MEDIA/EVENT/ADVERTISEMENT/OTHER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadStatus status;      // NEW/CONTACTED/QUALIFIED/CONVERTED/DISQUALIFIED >> canTransitionTo(target)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "converted_customer_id")
    private Long convertedCustomerId;

    @Column(name = "converted_at")
    private Instant convertedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
}