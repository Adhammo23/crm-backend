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

    @Test
    void convertToCustomer_succeedsForQualifiedLead() {
        Lead lead = TestFixtures.leadOwnedBy(salesEmployee, LeadStatus.QUALIFIED);

        when(leadRepository.findById(lead.getId())).thenReturn(Optional.of(lead));
        when(customerRepository.existsByEmail(lead.getEmail())).thenReturn(false);
        when(userRepository.findById(salesEmployee.getId())).thenReturn(Optional.of(salesEmployee));
        when(currentUserService.getCurrentUser()).thenReturn(manager);
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(leadRepository.updateStatusIfMatches(
                eq(lead.getId()), eq(LeadStatus.QUALIFIED), eq(LeadStatus.CONVERTED), any()))
                .thenReturn(1);
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(mock(CustomerResponse.class));

        assertDoesNotThrow(() -> leadService.convertToCustomer(lead.getId(), salesEmployee.getId()));

        verify(customerRepository).save(any(Customer.class));
        verify(leadRepository).updateStatusIfMatches(
                eq(lead.getId()), eq(LeadStatus.QUALIFIED), eq(LeadStatus.CONVERTED), any());
    }

    @Test
    void convertToCustomer_throwsWhenLeadNotQualified() {
        Lead lead = TestFixtures.leadOwnedBy(salesEmployee, LeadStatus.CONTACTED); // <<<

        when(leadRepository.findById(lead.getId())).thenReturn(Optional.of(lead));

        assertThrows(InvalidLeadStatusException.class,
                () -> leadService.convertToCustomer(lead.getId(), salesEmployee.getId()));

        verifyNoInteractions(customerRepository);
    }

    @Test
    void convertToCustomer_throwsWhenCustomerEmailAlreadyExists() {
        Lead lead = TestFixtures.leadOwnedBy(salesEmployee, LeadStatus.QUALIFIED);

        when(leadRepository.findById(lead.getId())).thenReturn(Optional.of(lead));
        when(customerRepository.existsByEmail(lead.getEmail())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class,
                () -> leadService.convertToCustomer(lead.getId(), salesEmployee.getId()));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void convertToCustomer_throwsWhenLeadAlreadyConvertedConcurrently() {
        Lead lead = TestFixtures.leadOwnedBy(salesEmployee, LeadStatus.QUALIFIED);

        when(leadRepository.findById(lead.getId())).thenReturn(Optional.of(lead));
        when(customerRepository.existsByEmail(lead.getEmail())).thenReturn(false);
        when(userRepository.findById(salesEmployee.getId())).thenReturn(Optional.of(salesEmployee));
        when(currentUserService.getCurrentUser()).thenReturn(manager);
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(leadRepository.updateStatusIfMatches(any(), any(), any(), any())).thenReturn(0);

        assertThrows(InvalidLeadStatusException.class,
                () -> leadService.convertToCustomer(lead.getId(), salesEmployee.getId()));
    }
}
