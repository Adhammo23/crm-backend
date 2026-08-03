package com.adham.crm_backend.lead;

import com.adham.crm_backend.customer.dto.CustomerResponse;
import com.adham.crm_backend.common.documentation.annotation.CreateApiResponses;
import com.adham.crm_backend.common.documentation.annotation.GetApiResponses;
import com.adham.crm_backend.common.documentation.annotation.UpdateApiResponses;
import com.adham.crm_backend.lead.dto.CreateLeadRequest;
import com.adham.crm_backend.lead.dto.LeadResponse;
import com.adham.crm_backend.lead.dto.UpdateLeadRequest;
import com.adham.crm_backend.lead.specification.LeadSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;

    @CreateApiResponses
    @Operation(summary = "Create a new lead")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(leadService.createLead(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    @UpdateApiResponses
    @Operation(summary = "Update lead")
    public ResponseEntity<LeadResponse> UpdateLead(@Valid @RequestBody UpdateLeadRequest request, @PathVariable Long id){
        return ResponseEntity.ok(leadService.update(id,request));
    }

    @GetApiResponses
    @Operation(summary = "Get lead by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<LeadResponse> getLeadById(@PathVariable Long id){
        return ResponseEntity.ok(leadService.getById(id));
    }
    @GetApiResponses
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    @GetMapping("/search")
    @Operation(summary = "Search leads",
            description = "Search leads using dynamic filters such as" +
                    " full name, email, job title, company name, owner ID and source. " +
                    "Results are restricted based on the authenticated user's role and data access scope."
    )
    public ResponseEntity<Page<LeadResponse>> search(@ModelAttribute LeadSearchRequest request, Pageable pageable){
        return ResponseEntity.ok(leadService.search(request,pageable));
    }

    @PatchMapping("/{leadId}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    @UpdateApiResponses
    @Operation(summary = "Transition status for lead")
    public ResponseEntity<LeadResponse> transitionStatus(@RequestBody LeadStatus status,@PathVariable Long leadId){
        return ResponseEntity.ok(leadService.transitionStatus(status,leadId));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/{leadId}/convert")
    @CreateApiResponses
    @Operation(
            summary = "Convert a qualified lead to a customer",
            description = "Creates a new customer based on the lead's information, "
                    + "sets the lead status to CONVERTED, and links the new customer to the lead. "
                    + "Only accessible by users with MANAGER or ADMIN roles. "
                    + "The lead must be in QUALIFIED status to be converted."
    )
    public ResponseEntity<CustomerResponse> convertToCustomer(@PathVariable Long leadId, @RequestParam Long ownerId){
       return ResponseEntity.status(HttpStatus.CREATED).body(leadService.convertToCustomer(leadId,ownerId));
    }
}
