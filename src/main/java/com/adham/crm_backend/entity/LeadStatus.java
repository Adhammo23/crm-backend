package com.adham.crm_backend.entity;

import lombok.Getter;

import java.util.Map;
import java.util.Set;

public enum LeadStatus {
    NEW, CONTACTED, QUALIFIED, CONVERTED, DISQUALIFIED;

    @Getter
    private static final Map<LeadStatus, Set<LeadStatus>> TRANSITIONS = Map.of(
            NEW,    Set.of(CONTACTED,DISQUALIFIED),
            CONTACTED,  Set.of(QUALIFIED, DISQUALIFIED),
            QUALIFIED,  Set.of(CONVERTED, DISQUALIFIED),
            CONVERTED,    Set.of(),
            DISQUALIFIED, Set.of()
    );

    public boolean canTransitionTo(LeadStatus target) {
        return TRANSITIONS.get(this /*current status*/ ).contains(target);
    }

}
