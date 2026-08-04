package com.adham.crm_backend.common;

import com.adham.crm_backend.TestFixtures;
import com.adham.crm_backend.common.security.AssertControl;
import com.adham.crm_backend.common.security.CurrentUserService;
import com.adham.crm_backend.customer.Customer;
import com.adham.crm_backend.team.Team;
import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssertControlTest {

    @Mock
    private CurrentUserService currentUserService;

    private AssertControl assertControl;
    @BeforeEach
    void setUp() {
         assertControl = new AssertControl(currentUserService);
    }

    @ParameterizedTest
    @EnumSource(value = RoleName.class, names = {"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SALES_EMPLOYEE"})
    void ownerCanAlwaysAccessTheirOwnRecord(RoleName role) {
        User user = TestFixtures.userWithRole(role);
        Customer customer = TestFixtures.customerOwnedBy(user);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        assertDoesNotThrow(() -> assertControl.assertCanAccess(customer));
    }


    @Test
    void salesEmployeeCannotAccessSomeoneElseRecord() {
        User owner = TestFixtures.salesEmployee(null);
        User otherSalesEmployee = TestFixtures.salesEmployee(null);
        Customer customer = TestFixtures.customerOwnedBy(owner);

        when(currentUserService.getCurrentUser()).thenReturn(otherSalesEmployee);

        assertThrows(AccessDeniedException.class,
                () -> assertControl.assertCanAccess(customer));
    }

    @Test
    void managerCanAccessTeamMembersRecord() {
        User manager = TestFixtures.manager();
        Team team = TestFixtures.teamManagedBy(manager);
        User salesEmployee = TestFixtures.salesEmployee(team);
        Customer customer = TestFixtures.customerOwnedBy(salesEmployee);

        when(currentUserService.getCurrentUser()).thenReturn(manager);

        assertDoesNotThrow(() -> assertControl.assertCanAccess(customer));
    }

    @Test
    void managerCannotAccessRecordOutsideTheirTeam() {
        User manager = TestFixtures.manager();
        User otherManager = TestFixtures.manager();
        Team otherTeam = TestFixtures.teamManagedBy(otherManager);
        User salesEmployee = TestFixtures.salesEmployee(otherTeam);
        Customer customer = TestFixtures.customerOwnedBy(salesEmployee);

        when(currentUserService.getCurrentUser()).thenReturn(manager);

        assertThrows(AccessDeniedException.class,
                () -> assertControl.assertCanAccess(customer));
    }
}
