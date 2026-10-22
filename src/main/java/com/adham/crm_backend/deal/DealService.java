package com.adham.crm_backend.deal;

import com.adham.crm_backend.common.exception.BusinessConflictException;
import com.adham.crm_backend.common.exception.ResourceNotFoundException;
import com.adham.crm_backend.common.security.AssertControl;
import com.adham.crm_backend.common.security.CurrentUserService;
import com.adham.crm_backend.common.util.OwnerResolver;
import com.adham.crm_backend.customer.Customer;
import com.adham.crm_backend.customer.CustomerRepository;
import com.adham.crm_backend.customer.CustomerStatus;
import com.adham.crm_backend.deal.dto.DealRequest;
import com.adham.crm_backend.deal.dto.DealResponse;
import com.adham.crm_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DealService {
    private final CurrentUserService currentUserService;
    private final OwnerResolver ownerResolver;
    private final DealRepository dealRepository;
    private final CustomerRepository customerRepository;
    private final AssertControl assertControl;
    private final DealMapper mapper;


    @Transactional
    public DealResponse create(DealRequest request){
        User currentUser = currentUserService.getCurrentUser();
        User owner = ownerResolver.resolveOwner(currentUser, request.getOwnerId());

       Customer customer =  getCustomer(request.getCustomerId());
       if (!(customer.getStatus().equals(CustomerStatus.ACTIVE))){
           throw new BusinessConflictException("Cannot create a deal for a customer with status: " + customer.getStatus());
       }
       assertControl.assertCanAccess(customer);

       Deal deal = Deal.builder()
               .customer(customer)
               .owner(owner)
               .createdBy(currentUser)
               .amount(request.getAmount())
               .title(request.getTitle())
               .status(DealStatus.IN_PROGRESS)
               .expectedCloseDate(request.getExpectedCloseDate())
               .build();

       return mapper.response(dealRepository.save(deal));
    }

    @Transactional
    public void dealWon(Long dealId) { closeDeal(dealId, DealStatus.WON); }

    @Transactional
    public void dealLost(Long dealId) { closeDeal(dealId, DealStatus.LOST); }

    private Deal getDeal(Long dealId){
        return dealRepository.findById(dealId).orElseThrow(()-> new ResourceNotFoundException("Deal not found"));
    }
    private Customer getCustomer(Long customerId){
        return customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
    }
    private void dealStatusTransition(Deal deal, DealStatus newStatus){
        if (!deal.getStatus().canTransitionTo(newStatus)){
            throw new BusinessConflictException("Cannot transition deal status from " + deal.getStatus() + " to " + newStatus);
        }
        deal.setStatus(newStatus);
    }
    private void closeDeal(Long dealId, DealStatus newStatus) {
        Deal deal = getDeal(dealId);
        assertControl.assertCanAccess(deal);
        dealStatusTransition(deal, newStatus);
        deal.setClosedAt(Instant.now());
    }
}

