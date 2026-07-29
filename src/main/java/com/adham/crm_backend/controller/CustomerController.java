package com.adham.crm_backend.controller;

import com.adham.crm_backend.documentation.annotation.CreateApiResponses;
import com.adham.crm_backend.documentation.annotation.GetApiResponses;
import com.adham.crm_backend.documentation.annotation.UpdateApiResponses;
import com.adham.crm_backend.dto.CreateCustomerRequest;
import com.adham.crm_backend.dto.CustomerResponse;
import com.adham.crm_backend.dto.ReassignCustomerRequest;
import com.adham.crm_backend.dto.UpdateCustomerRequest;
import com.adham.crm_backend.service.CustomerService;
import com.adham.crm_backend.specification.CustomerSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customers")
@Tag(name = "Customers", description = "Customer management endpoints")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @CreateApiResponses
    @Operation(summary = "Create a new customer")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }
    @GetApiResponses
    @Operation(summary = "Get all customers visible to the current user")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }
    @GetApiResponses
    @Operation(summary = "Get customer by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    @UpdateApiResponses
    @Operation(summary = "Update customer")
    public ResponseEntity<CustomerResponse> UpdateCustomer (@Valid @RequestBody UpdateCustomerRequest request, @PathVariable Long id){
        return ResponseEntity.ok(customerService.updateCustomer(request,id));
    }
    @PatchMapping("/{id}/reassign")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @UpdateApiResponses
    @Operation(summary = "Reassign customer")
    public ResponseEntity<CustomerResponse> reassignCustomer(@Valid @RequestBody ReassignCustomerRequest request,@PathVariable Long id){
        return ResponseEntity.ok(customerService.reassignCustomer(request,id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers",
            description = "Search customers using dynamic filters such as" +
                    " full name, email, job title, company name, and owner ID. " +
                    "Results are restricted based on the authenticated user's role and data access scope."
    )
    @GetApiResponses
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Page<CustomerResponse>> search(@ModelAttribute CustomerSearchRequest request, Pageable pageable){
        return ResponseEntity.ok(customerService.search(request,pageable));
    }


}
