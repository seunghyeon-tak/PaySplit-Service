package com.paysplit.api.service;

import com.paysplit.common.error.settlement.SettlementException;
import com.paysplit.db.domain.Payment;
import com.paysplit.db.domain.Settlement;
import com.paysplit.db.enums.SettlementStatus;
import com.paysplit.db.enums.SettlementType;
import com.paysplit.db.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.paysplit.common.error.settlement.SettlementErrorCode.ALREADY_SETTLED_PAYMENT;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private final SettlementRepository settlementRepository;

    public Settlement createSettlement(Payment payment) {
        // 이미 정산된 결제인지 확인
        if (settlementRepository.existsByPayment(payment)) {
            throw new SettlementException(ALREADY_SETTLED_PAYMENT);
        }

        // settlement Entity 생성
        Settlement settlement = Settlement.builder()
                .payment(payment)
                .status(SettlementStatus.READY)
                .type(SettlementType.NORMAL)
                .totalAmount(payment.getAmount())
                .build();

        // 저장
        return settlementRepository.save(settlement);
    }
}
