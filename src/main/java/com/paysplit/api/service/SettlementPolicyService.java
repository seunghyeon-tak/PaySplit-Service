package com.paysplit.api.service;

import com.paysplit.db.domain.Settlement;
import com.paysplit.db.repository.SettlementPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettlementPolicyService {
    private final SettlementPolicyRepository settlementPolicyRepository;

    public void applyPolicy(Settlement settlement) {
    }
}
