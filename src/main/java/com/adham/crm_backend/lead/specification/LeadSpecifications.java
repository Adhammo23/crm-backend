package com.adham.crm_backend.lead.specification;


import com.adham.crm_backend.lead.Lead;
import org.springframework.data.jpa.domain.Specification;

public class LeadSpecifications {

    public static Specification<Lead> searchSpec(LeadSearchRequest request){

        Specification<Lead> spec =Specification.unrestricted();

        if (request.getFullName()!=null){
            spec = spec.and(hasFullName(request.getFullName()));
        }

        if (request.getEmail()!= null){
            spec=spec.and(hasEmail(request.getEmail()));
        }

        if (request.getCompanyName()!=null){
            spec=spec.and(hasCompanyName(request.getCompanyName()));
        }

        if (request.getOwnerId()!=null){
            spec=spec.and(hasOwnerId(request.getOwnerId()));
        }

        if (request.getSource()!=null){
            spec=spec.and(hasSource(request.getSource()));
        }
        return spec;
    }


    private static Specification<Lead> hasFullName (String fullName){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("fullName")),"%"+fullName.toLowerCase()+"%"
                ));
    }
    private static Specification<Lead> hasEmail (String email){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),"%"+email.toLowerCase()+"%"
                ));
    }

    private static Specification<Lead> hasJopTitle(String jopTitle){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")),"%"+jopTitle.toLowerCase()+"%"
                ));
    }

    private static Specification<Lead> hasCompanyName(String companyName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("companyName")),
                        "%" + companyName.toLowerCase() + "%"
                );
    }

    private static Specification<Lead> hasOwnerId(Long ownerId){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("id"),ownerId
                ));
    }
    private static Specification<Lead> hasSource(String source){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("source"),"%" + source.toUpperCase()+"%"
                );
    }
}
