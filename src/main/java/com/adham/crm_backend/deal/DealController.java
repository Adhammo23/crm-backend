package com.adham.crm_backend.deal;

import com.adham.crm_backend.common.documentation.annotation.UpdateApiResponses;
import com.adham.crm_backend.deal.dto.DealRequest;
import com.adham.crm_backend.deal.dto.DealResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<DealResponse> create(@Valid @RequestBody DealRequest request){
        return ResponseEntity.ok().body(dealService.create(request));
    }
    @PatchMapping("/{dealId}/won")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    @UpdateApiResponses
    @Operation(summary = "Mark deal as won")
    public ResponseEntity<Void> dealWon(@PathVariable Long dealId) {
        dealService.dealWon(dealId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{dealId}/lost")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_SALES_EMPLOYEE', 'ROLE_ADMIN')")
    @UpdateApiResponses
    @Operation(summary = "Mark deal as lost")
    public ResponseEntity<Void> dealLost(@PathVariable Long dealId) {
        dealService.dealLost(dealId);
        return ResponseEntity.noContent().build();
    }
}
