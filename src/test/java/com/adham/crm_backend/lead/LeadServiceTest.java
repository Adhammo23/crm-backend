package com.adham.crm_backend.lead;

import com.adham.crm_backend.TestFixtures;
import com.adham.crm_backend.common.exception.InvalidLeadStatusException;
import com.adham.crm_backend.common.exception.LeadAlreadyExistsException;
import com.adham.crm_backend.common.exception.MissingOwnerException;
import com.adham.crm_backend.common.security.AssertControl;
import com.adham.crm_backend.common.security.CurrentUserService;
import com.adham.crm_backend.common.util.OwnerResolver;
import com.adham.crm_backend.customer.Customer;
import com.adham.crm_backend.customer.CustomerMapper;
import com.adham.crm_backend.customer.CustomerRepository;
import com.adham.crm_backend.customer.dto.CustomerResponse;
import com.adham.crm_backend.customer.exception.CustomerAlreadyExistsException;
import com.adham.crm_backend.lead.dto.CreateLeadRequest;
import com.adham.crm_backend.lead.dto.LeadResponse;
import com.adham.crm_backend.user.entity.RoleName;
import com.adham.crm_backend.user.entity.User;
import com.adham.crm_backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeadServiceTest {

    @Mock private LeadRepository leadRepository;
    @Mock private CurrentUserService currentUserService;
    @Mock private OwnerResolver ownerResolver;
    @Mock private LeadMapper leadMapper;
    @Mock private CustomerRepository customerRepository;
    @Mock private UserRepository userRepository;
    @Mock private CustomerMapper customerMapper;
    @Mock private AssertControl assertControl;
    @InjectMocks
    private LeadService leadService;

    private User salesEmployee;
    private User manager;

    @BeforeEach
    void setUp(){
        salesEmployee = TestFixtures.userWithRole(RoleName.ROLE_SALES_EMPLOYEE);
        manager = TestFixtures.manager();

    }

    @Test
    void createLead_throwsWhenEmailAlreadyExists(){

        CreateLeadRequest request = new CreateLeadRequest();
        request.setEmail("duplicate@test.com");

        when(currentUserService.getCurrentUser()).thenReturn(salesEmployee);
        when(ownerResolver.resolveOwner(any(),any())).thenReturn(salesEmployee);
        when(leadRepository.existsByEmail("duplicate@test.com")).thenReturn(true);

        Exception exception = assertThrows(LeadAlreadyExistsException.class,() -> leadService.createLead(request));
        assertEquals("Lead already exists with email: "+request.getEmail(),exception.getMessage());
    }

    @Test
    void createLead_succeedsWhenEmailIsUnique(){

        CreateLeadRequest request = new CreateLeadRequest();
        request.setEmail("new@test.com");
        request.setFullName("New Lead");
        request.setLeadSource(LeadSource.WEBSITE);

        when(currentUserService.getCurrentUser()).thenReturn(salesEmployee);
        when(ownerResolver.resolveOwner(any(),any())).thenReturn(salesEmployee);
        when(leadRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(leadRepository.save(any(Lead.class))).thenAnswer(ob->ob.getArgument(0));
        when(leadMapper.toResponse(any(Lead.class))).thenReturn(mock(LeadResponse.class));

        assertDoesNotThrow( ()-> leadService.createLead(request));
    }

    @Test
    void createLead_throwsWhenOwnerCannotBeResolved(){

        CreateLeadRequest request = new CreateLeadRequest();
        request.setEmail("new@test.com");


        when(currentUserService.getCurrentUser()).thenReturn(manager);
        when(ownerResolver.resolveOwner(manager,null)).thenThrow(new MissingOwnerException("Owner ID is required"));

        assertThrows(MissingOwnerException.class,
                () -> leadService.createLead(request));

        verify(leadRepository, never())
                .save(any(Lead.class));

    }

}
