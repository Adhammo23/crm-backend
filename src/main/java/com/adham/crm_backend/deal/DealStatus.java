package com.adham.crm_backend.deal;

import java.util.Map;
import java.util.Set;

public enum DealStatus {
    IN_PROGRESS,
    WON,
    LOST;

    private static final Map<DealStatus, Set<DealStatus>> TRANSITIONS = Map.of(
            IN_PROGRESS,    Set.of(WON,LOST),
            WON,    Set.of(),
            LOST,   Set.of()
    );

    public boolean canTransitionTo(DealStatus target){
        return TRANSITIONS.get(this).contains(target);
    }
}
