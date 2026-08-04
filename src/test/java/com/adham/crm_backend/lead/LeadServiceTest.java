package com.adham.crm_backend.lead;

import com.adham.crm_backend.TestFixtures;
import com.adham.crm_backend.common.exception.LeadAlreadyExistsException;
import com.adham.crm_backend.common.security.AssertControl;
import com.adham.crm_backend.common.security.CurrentUserService;
import com.adham.crm_backend.common.specification.CustomerLeadAccessSpecifications;
import com.adham.crm_backend.common.util.OwnerResolver;
import com.adham.crm_backend.customer.CustomerMapper;
import com.adham.crm_backend.customer.CustomerRepository;
import com.adham.crm_backend.lead.dto.CreateLeadRequest;
import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.entity.User;
import com.adham.crm_backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeadServiceTest {

    @Mock private LeadRepository leadRepository;
    @Mock private CurrentUserService currentUserService;
    @Mock private OwnerResolver ownerResolver;

    @InjectMocks
    private LeadService leadService;

    private User salesEmployee;

    @BeforeEach
    void setUp(){
        salesEmployee = TestFixtures.userWithRole(RoleName.ROLE_SALES_EMPLOYEE);
    }

    @Test
    void createLead_throwsWhenEmailAlreadyExists(){

        // Arrange
        CreateLeadRequest request = new CreateLeadRequest();
        request.setEmail("duplicate@test.com");

        // Act
        when(currentUserService.getCurrentUser()).thenReturn(salesEmployee);
        when(ownerResolver.resolveOwner(any(),any())).thenReturn(salesEmployee);
        when(leadRepository.existsByEmail("duplicate@test.com")).thenReturn(true);

        // Assert
       Exception exception = assertThrows(LeadAlreadyExistsException.class,() -> leadService.createLead(request));
        assertEquals("Lead already exists with email: "+request.getEmail(),exception.getMessage());
    }
}
