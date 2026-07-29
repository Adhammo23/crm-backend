package com.adham.crm_backend.specification;

import com.adham.crm_backend.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

    public static Specification<Customer> search(
            CustomerSearchRequest request
    ) {

        Specification<Customer> spec =
                Specification.unrestricted();

        if (request.getFullName() != null &&
                !request.getFullName().isBlank()) {

            spec = spec.and(hasFullName(request.getFullName()));
        }

        if (request.getEmail() != null &&
                !request.getEmail().isBlank()) {

            spec = spec.and(hasEmail(request.getEmail()));
        }

        if (request.getJobTitle() != null &&
                !request.getJobTitle().isBlank()) {

            spec = spec.and(hasJobTitle(request.getJobTitle()));
        }

        if (request.getCompanyName() != null &&
                !request.getCompanyName().isBlank()) {

            spec = spec.and(hasCompanyName(request.getCompanyName()));
        }

        if (request.getOwnerId() != null) {

            spec = spec.and(hasOwnerId(request.getOwnerId()));
        }

        return spec;
    }


    private static Specification<Customer> hasFullName(String fullName){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("fullName")),
                                "%" + fullName.toLowerCase()+"%"));
    }


    private static Specification<Customer> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                );
    }


    private static Specification<Customer> hasJobTitle(String jobTitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("jobTitle")),
                        "%" + jobTitle.toLowerCase() + "%"
                );
    }


    private static Specification<Customer> hasCompanyName(String companyName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("companyName")),
                        "%" + companyName.toLowerCase() + "%"
                );
    }


    private static Specification<Customer> hasOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("owner").get("id"),
                        ownerId
                );
    }
}