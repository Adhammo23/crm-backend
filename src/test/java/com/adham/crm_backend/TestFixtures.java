package com.adham.crm_backend;

import com.adham.crm_backend.customer.Customer;
import com.adham.crm_backend.customer.CustomerStatus;
import com.adham.crm_backend.lead.Lead;
import com.adham.crm_backend.lead.LeadSource;
import com.adham.crm_backend.lead.LeadStatus;
import com.adham.crm_backend.team.Team;
import com.adham.crm_backend.user.entity.Role;
import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.entity.User;

import java.util.Set;

public class TestFixtures {

    private static long idCounter = 1;

    private static Long nextId() {
        return idCounter++;
    }

    // ---------- Users ----------

    public static User userWithRole(RoleName roleName) {
        return User.builder()
                .id(nextId())
                .fullName("Test " + roleName)
                .email(roleName.name().toLowerCase() + "@test.com")
                .roles(Set.of(Role.builder().name(roleName).build()))
                .build();
    }

    public static User salesEmployee(Team team) {
        User user = userWithRole(RoleName.ROLE_SALES_EMPLOYEE);
        user.setTeam(team);
        return user;
    }

    public static User manager() {
        return userWithRole(RoleName.ROLE_MANAGER);
    }

    public static User admin() {
        return userWithRole(RoleName.ROLE_ADMIN);
    }

    // ---------- Teams ----------

    public static Team teamManagedBy(User manager) {
        return Team.builder()
                .id(nextId())
                .name("Test Team")
                .manager(manager)
                .build();
    }

    // ---------- Customers ----------

    public static Customer customerOwnedBy(User owner) {
        return Customer.builder()
                .id(nextId())
                .fullName("Test Customer")
                .email("customer@test.com")
                .status(CustomerStatus.ACTIVE)
                .owner(owner)
                .build();
    }

    // ---------- Leads ----------

    public static Lead leadOwnedBy(User owner, LeadStatus status) {
        return Lead.builder()
                .id(nextId())
                .fullName("Test Lead")
                .email("lead@test.com")
                .source(LeadSource.WEBSITE)
                .status(status)
                .owner(owner)
                .build();
    }
}
