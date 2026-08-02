package com.adham.crm_backend.security;

import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.exception.InvalidCustomerOwnerException;
import com.adham.crm_backend.exception.MissingOwnerException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssertControl {

    private final CurrentUserService currentUserService;

        public  void assertCanAccess(HasOwner entity){
            User currentUser = currentUserService.getCurrentUser() ;

            if (currentUser.hasRole(RoleName.ROLE_ADMIN)) return;
            if (currentUser.hasRole(RoleName.ROLE_MANAGER)){
                boolean isOwnCustomer = entity
                        .getOwner()
                        .getId()
                        .equals(currentUser.getId());
                boolean isTeamCustomer = entity
                        .getOwner()
                        .getTeam() != null && entity
                        .getOwner()
                        .getTeam()
                        .getManager() !=null
                        && entity
                        .getOwner()
                        .getTeam()
                        .getManager()
                        .getId()
                        .equals(currentUser.getId());
                if (isOwnCustomer || isTeamCustomer ) return;
                throw new AccessDeniedException("You do not have access");
            }
            throw new AccessDeniedException(
                    "You do not have permission to reassign customers.");
        }

    public void assertValidOwner(User owner) {

        if (!owner.hasRole(RoleName.ROLE_SALES_EMPLOYEE)) {
            throw new MissingOwnerException(
                    "Customer owner must be a sales employee."
            );
        }
        if (owner.getTeam() == null){
            throw new InvalidCustomerOwnerException("Customer owner must be assigned to a team");
        }
    }
}
