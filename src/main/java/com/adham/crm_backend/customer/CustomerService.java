package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.CreateCustomerRequest;
import com.adham.crm_backend.dto.CustomerResponse;
import com.adham.crm_backend.dto.ReassignCustomerRequest;
import com.adham.crm_backend.dto.UpdateCustomerRequest;
import com.adham.crm_backend.entity.Customer;
import com.adham.crm_backend.entity.CustomerStatus;
import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.exception.*;
import com.adham.crm_backend.mapper.CustomerMapper;
import com.adham.crm_backend.repository.CustomerRepository;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.SecurityUtils;
import com.adham.crm_backend.specification.CustomerAccessSpecifications;
import com.adham.crm_backend.specification.CustomerSearchRequest;
import com.adham.crm_backend.specification.CustomerSpecifications;
import com.adham.crm_backend.common.security.AssertControl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final CustomerAccessSpecifications customerAccessSpecification;
    private final AssertControl assertControl;
    private final OwnerResolver ownerResolver;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        User currentUser = getCurrentDomainUser();

        User owner = ownerResolver.resolveOwner(currentUser, request.getOwnerId());

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already exists with email: " + request.getEmail());
        }
            Customer customer = Customer.builder()
                    .fullName(request.getFullName())
                    .companyName(request.getCompanyName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .jobTitle(request.getJobTitle())
                    .status(CustomerStatus.ACTIVE)
                    .owner(owner)
                    .createdBy(currentUser)
                    .build();
            return customerMapper.toResponse(customerRepository.save(customer));

    }

    public Page<CustomerResponse> getAllCustomers(Pageable pageable){

        User currentUser = getCurrentDomainUser();

        if (currentUser.hasRole(RoleName.ROLE_ADMIN)){
            return customerRepository.findAll(pageable).map(customerMapper::toResponse);
        }
        if (currentUser.hasRole(RoleName.ROLE_MANAGER)){
            return customerRepository.findVisibleToManager(currentUser,pageable).map(customerMapper::toResponse);
        }

        return customerRepository.findByOwner(currentUser, pageable)
                .map(customerMapper::toResponse);

    }
    public CustomerResponse getCustomerById(Long id){
        Customer customer = findCustomerById(id);
        assertControl.assertCanAccess(customer);
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UpdateCustomerRequest request, Long id){

        Customer customer = findCustomerById(id);

        assertControl.assertCanAccess(customer);

        if (request.fullName() != null){
            customer.setFullName(request.fullName());
        }
        if (request.companyName()!= null){
         customer.setCompanyName(request.companyName());
        }
        if (request.email() != null){
            boolean emailChanged = !customer.getEmail().equals(request.email());
            if(emailChanged && customerRepository.existsByEmail(request.email())){
                throw new CustomerAlreadyExistsException(
                        "Customer already exists with email: " + request.email());
            }
            customer.setEmail(request.email());
        }
        if (request.jobTitle() != null){
            customer.setJobTitle(request.jobTitle());
        }
        if (request.phone() != null){
            customer.setPhone(request.phone());
        }

        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse reassignCustomer(ReassignCustomerRequest request, Long id){
        Customer customer = findCustomerById(id);

        assertControl.assertCanReassign(customer);

        User newOwner = findUserById(request.ownerId());
        assertControl.assertValidOwner(newOwner);
        if (!(newOwner.getTeam().getId()
                .equals(customer.getOwner().getTeam().getId()))){
            throw new CustomerReassignmentException("the New owner should be in same team as the old owner");
        }

        customer.setOwner(newOwner);
        return customerMapper.toResponse(customer);
    }
    public Page<CustomerResponse> search( CustomerSearchRequest request, Pageable pageable){

        return customerRepository.
                findAll(customerAccessSpecification.forCurrentUser()
                        .and(CustomerSpecifications.search(request))
                        ,pageable)
                .map(customerMapper::toResponse);
    }
    // =======Helper methods=======
    private Customer findCustomerById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with Id: " + id));
    }
    private User findUserById(Long id){
        return userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User not found"));
    }
    private User getCurrentDomainUser() {
        String email = SecurityUtils.getCurrentUser().getUsername();
        return userRepository.findByEmailWithRoles(email).orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));
    }
}
