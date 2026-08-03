package com.adham.crm_backend.common.util;

import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.entity.User;
import com.adham.crm_backend.common.exception.MissingOwnerException;
import com.adham.crm_backend.common.exception.ResourceNotFoundException;
import com.adham.crm_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerResolver {
    private final UserRepository userRepository;

    /**
     * Determines the owner for a new Lead or Customer based on the current user's role.
     * - If current user is Sales Employee, they automatically become the owner.
     * - If current user is Admin/Manager, they must specify an ownerId.
     */
    public User resolveOwner(User currentUser, Long requestedOwnerId) {
        // Sales employees can only own the entity themselves
        if (currentUser.hasRole(RoleName.ROLE_SALES_EMPLOYEE)
                && !currentUser.hasRole(RoleName.ROLE_ADMIN)
                && !currentUser.hasRole(RoleName.ROLE_MANAGER)) {
            return currentUser;
        }

        // Admins and Managers must provide an owner ID
        if (requestedOwnerId == null) {
            throw new MissingOwnerException(
                    "Owner ID is required when creating a Lead/Customer as Manager or Admin."
            );
        }

        return userRepository.findById(requestedOwnerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + requestedOwnerId
                ));
    }
}
