package com.adham.crm_backend.specification;
import com.adham.crm_backend.entity.Customer;
import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.Team;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.SecurityUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLeadAccessSpecifications {

    private final UserRepository userRepository;
    public <T> Specification<T  > forCurrentUser(){

        User currentUser = getCurrentDomainUser();
        if (currentUser.hasRole(RoleName.ROLE_ADMIN)){
            return Specification.unrestricted();
        }

        if (currentUser.hasRole(RoleName.ROLE_MANAGER)){
            return ownedByManagerOrTeam(currentUser);
        }

        return ownedBy(currentUser);
    }

    public static <T> Specification<T> ownedBy(User user){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("owner").get("id"),user.getId());
    }

    public static <T> Specification<T> ownedByManagerOrTeam(User manager) {

        return (root, query, criteriaBuilder) -> {
            Join<T, User> owner =
                    root.join("owner", JoinType.LEFT);

            Join<User, Team> team = owner.join("team",JoinType.LEFT);

            return criteriaBuilder.or(criteriaBuilder.equal(owner.get("id"), manager.getId()),
                    criteriaBuilder.equal(team.get("manager").get("id"), manager.getId()));
        };
    }

    private User getCurrentDomainUser() {
        String email = SecurityUtils.getCurrentUser().getUsername();
        return userRepository.findByEmailWithRoles(email).orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));
    }
}
