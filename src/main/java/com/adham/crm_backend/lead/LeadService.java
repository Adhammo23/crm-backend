package com.adham.crm_backend.lead;

import com.adham.crm_backend.customer.exception.CustomerAlreadyExistsException;
import com.adham.crm_backend.common.exception.InvalidLeadStatusException;
import com.adham.crm_backend.common.exception.LeadAlreadyExistsException;
import com.adham.crm_backend.common.exception.ResourceNotFoundException;
import com.adham.crm_backend.common.util.OwnerResolver;
import com.adham.crm_backend.customer.Customer;
import com.adham.crm_backend.customer.CustomerStatus;
import com.adham.crm_backend.lead.dto.CreateLeadRequest;
import com.adham.crm_backend.customer.dto.CustomerResponse;
import com.adham.crm_backend.lead.dto.LeadResponse;
import com.adham.crm_backend.lead.dto.UpdateLeadRequest;
import com.adham.crm_backend.customer.CustomerMapper;
import com.adham.crm_backend.customer.CustomerRepository;
import com.adham.crm_backend.user.entity.User;
import com.adham.crm_backend.user.repository.UserRepository;
import com.adham.crm_backend.common.security.AssertControl;
import com.adham.crm_backend.common.security.CurrentUserService;
import com.adham.crm_backend.common.specification.CustomerLeadAccessSpecifications;
import com.adham.crm_backend.lead.specification.LeadSearchRequest;
import com.adham.crm_backend.lead.specification.LeadSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeadService {
    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final AssertControl assertControl;
    private final CustomerLeadAccessSpecifications customerLeadAccessSpecifications;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OwnerResolver ownerResolver;

    @Transactional
    public LeadResponse createLead(CreateLeadRequest request){
        User currentUser = currentUserService.getCurrentUser();

        User owner = ownerResolver.resolveOwner(currentUser, request.getOwnerId());

        if (leadRepository.existsByEmail(request.getEmail())) {
            throw new LeadAlreadyExistsException(
                    "Lead already exists with email: " + request.getEmail());
        }

        Lead lead = Lead.builder()
                .fullName(request.getFullName())
                .companyName(request.getCompanyName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .jobTitle(request.getJobTitle())
                .status(LeadStatus.NEW)
                .source(request.getLeadSource())
                .owner(owner)
                .createdBy(currentUser)
                .build();

        return leadMapper.toResponse(leadRepository.save(lead));
    }

    @Transactional
    public LeadResponse update(Long id, UpdateLeadRequest request){
         Lead lead = leadRepository.findById(id)
                 .orElseThrow(()-> new ResourceNotFoundException("Lead not found to update"));
        assertControl.assertCanAccess(lead);

        if (request.fullName() != null){
            lead.setFullName(request.fullName());
        }
        if (request.companyName()!= null){
            lead.setCompanyName(request.companyName());
        }
        if (request.email() != null){
            boolean emailChanged = !lead.getEmail().equals(request.email());
            if(emailChanged && leadRepository.existsByEmail(request.email())){
                throw new LeadAlreadyExistsException(
                        "Lead already exists with email: " + request.email());
            }
            lead.setEmail(request.email());
        }
        if (request.jobTitle() != null){
            lead.setJobTitle(request.jobTitle());
        }
        if (request.phone() != null){
            lead.setPhone(request.phone());
        }

        return leadMapper.toResponse(lead);
    }

    public LeadResponse getById(Long id){
        Lead lead = leadRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Lead not found with id: " +id));

        assertControl.assertCanAccess(lead);

        return leadMapper.toResponse(lead);
    }

    public Page<LeadResponse> search(LeadSearchRequest  request, Pageable pageable){
        Specification<Lead> specificationAccess = customerLeadAccessSpecifications.forCurrentUser();
        Specification<Lead> specificationSearch = LeadSpecifications.searchSpec(request);

        Specification<Lead> specification = specificationSearch.and(specificationAccess);

        return leadRepository.findAll(specification, pageable).map(leadMapper::toResponse);
    }
    @Transactional
    public LeadResponse transitionStatus(LeadStatus newStatus, Long id){

        Lead lead = leadRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("Lead not found with id: "+id));

        assertControl.assertCanAccess(lead);

        if (!lead.getStatus().canTransitionTo(newStatus)){
            LeadStatus currentStatus = lead.getStatus();
            throw new InvalidLeadStatusException("Transition from %s to %s not allowed.".formatted(currentStatus,newStatus));
        }

        lead.setStatus(newStatus);

        return leadMapper.toResponse(lead);
    }

    @Transactional
    public CustomerResponse convertToCustomer(Long id, Long ownerId){

        User currentUser = currentUserService.getCurrentUser();

        Lead lead = leadRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("Lead not found with id: " +id));

        assertControl.assertCanAccess(lead);

        if (!lead.getStatus().equals(LeadStatus.QUALIFIED)){
            throw new InvalidLeadStatusException("Only qualified leads can be converted.");
        }

        if (customerRepository.existsByEmail(lead.getEmail())){
            throw new CustomerAlreadyExistsException("Customer already exists with email: "+lead.getEmail());
        }

        Customer.CustomerBuilder customer = Customer.builder()
                .fullName(lead.getFullName())
                .jobTitle(lead.getJobTitle())
                .phone(lead.getPhone())
                .email(lead.getEmail())
                .status(CustomerStatus.ACTIVE)
                .companyName(lead.getCompanyName())
                .createdBy(currentUser);

        User owner = findUserById(ownerId);
        assertControl.assertValidOwner(owner);

        customer.owner(owner);
        Customer savedCustomer = customerRepository.save(customer.build());


        // Atomically update the lead status to CONVERTED only if it is still QUALIFIED.
        // This prevents race conditions (two users converting the same lead simultaneously).
        // Returns 1 if updated successfully, 0 if the status changed concurrently.
        int updatedRows = leadRepository.updateStatusIfMatches(
                id,
                LeadStatus.QUALIFIED,
                LeadStatus.CONVERTED,
                savedCustomer.getId()
        );
        if (updatedRows == 0) {
            throw new InvalidLeadStatusException("This lead has already been converted by another user.");
        }

        return customerMapper.toResponse(savedCustomer);
    }

    private User findUserById(Long id){
        return userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User not found"));
    }

}
