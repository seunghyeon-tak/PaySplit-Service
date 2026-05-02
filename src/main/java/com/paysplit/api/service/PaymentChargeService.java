package com.paysplit.api.service;

import com.paysplit.common.error.billing.BillingKeyErrorCode;
import com.paysplit.common.error.billing.BillingKeyException;
import com.paysplit.db.domain.BillingKey;
import com.paysplit.db.domain.User;
import com.paysplit.db.repository.BillingKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentChargeService {
    private final BillingKeyRepository billingKeyRepository;

    public BillingKey getByUser(User user) {
        return billingKeyRepository.findByUser(user)
                .orElseThrow(() -> new BillingKeyException(BillingKeyErrorCode.BILLING_KEY_NOT_FOUND));
    }
}
