package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.CreateLeadRequest;
import com.adham.crm_backend.dto.CustomerResponse;
import com.adham.crm_backend.dto.LeadResponse;
import com.adham.crm_backend.dto.UpdateLeadRequest;
import com.adham.crm_backend.entity.*;
import com.adham.crm_backend.exception.*;
import com.adham.crm_backend.mapper.CustomerMapper;
import com.adham.crm_backend.mapper.LeadMapper;
import com.adham.crm_backend.repository.CustomerRepository;
import com.adham.crm_backend.repository.LeadRepository;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.AssertControl;
import com.adham.crm_backend.security.CurrentUserService;
import com.adham.crm_backend.specification.CustomerLeadAccessSpecifications;
import com.adham.crm_backend.specification.LeadSearchRequest;
import com.adham.crm_backend.specification.LeadSpecifications;
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
    private final LeadStatus leadStatus;
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
                throw new CustomerAlreadyExistsException(
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
    public LeadResponse canTransitionTo(LeadStatus newStatus, Long id){

        Lead lead = leadRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("Lead not found with id: "+id));

        assertControl.assertCanAccess(lead);

        if (!leadStatus.canTransitionTo(newStatus)){
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
        lead.setStatus(LeadStatus.CONVERTED);

        lead.setConvertedCustomerId(savedCustomer.getId());

        return customerMapper.toResponse(savedCustomer);
    }

    private User findUserById(Long id){
        return userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User not found"));
    }

}
