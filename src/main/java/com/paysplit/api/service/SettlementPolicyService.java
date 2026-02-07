package com.paysplit.api.service;

import com.paysplit.api.dto.settlement.result.SettlementItemResult;
import com.paysplit.common.error.settlement.SettlementException;
import com.paysplit.db.domain.Settlement;
import com.paysplit.db.domain.SettlementPolicy;
import com.paysplit.db.enums.FeeType;
import com.paysplit.db.enums.ReceiverType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.paysplit.common.error.settlement.SettlementErrorCode.*;

@Service
@RequiredArgsConstructor
public class SettlementPolicyService {

    public List<SettlementItemResult> calculate(Settlement settlement) {

        BigDecimal totalAmount = settlement.getTotalAmount();
        SettlementPolicy policy = settlement.getSettlementPolicy();

        int partySize = 4; // TODO: 임의 값 추후 수정 필요
        if (partySize <= 0) {
            throw new SettlementException(INVALID_PARTY_SIZE);
        }

        // 1. 기본 1인 결제 금액
        BigDecimal basePayAmount = totalAmount.divide(
                BigDecimal.valueOf(partySize),
                0,
                RoundingMode.DOWN
        );

        // 2. 파티장 할인 금액 계산 (정책 기반)
        BigDecimal leaderDiscountAmount = calculatePolicyAmount(
                policy.getLeaderShareType(),
                policy.getLeaderShareValue(),
                totalAmount
        );

        BigDecimal leaderPayAmount = basePayAmount.subtract(leaderDiscountAmount);
        if (leaderPayAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new SettlementException(INVALID_DISTRIBUTION_AMOUNT);
        }

        // 3. 플랫폼 지원 금액 (파티장 할인과 동일)
        BigDecimal platformSupportAmount = leaderDiscountAmount;

        // 4. 정합성 검증
        BigDecimal calculatedTotal =
                basePayAmount.multiply(BigDecimal.valueOf(partySize - 1))
                        .add(leaderPayAmount)
                        .add(platformSupportAmount);

        if (calculatedTotal.compareTo(totalAmount) != 0) {
            throw new SettlementException(SETTLEMENT_AMOUNT_MISMATCH);
        }

        // 5. 결과 생성
        List<SettlementItemResult> results = new ArrayList<>();

        // 파티원 결제
        for (int i = 0; i < partySize - 1; i++) {
            results.add(
                    SettlementItemResult.builder()
                            .receiverType(ReceiverType.MEMBER_PAYMENT)
                            .receiverId(1000L + i)  // todo : 임시 유저 ID
                            .amount(basePayAmount)
                            .build()
            );
        }

        // 파티장 결제
        results.add(
                SettlementItemResult.builder()
                        .receiverType(ReceiverType.LEADER_PAYMENT)
                        .receiverId(999L)  // todo : 임시 파티장 ID
                        .amount(leaderPayAmount)
                        .build()
        );

        // 플랫폼 지원
        results.add(
                SettlementItemResult.builder()
                        .receiverType(ReceiverType.PLATFORM_SUPPORT)
                        .receiverId(0L)  // todo : 플랫폼은 고정 ID 일단 임시
                        .amount(platformSupportAmount)
                        .build()
        );

        return results;
    }

    private BigDecimal calculatePolicyAmount(
            FeeType type,
            BigDecimal value,
            BigDecimal totalAmount
    ) {
        if (type == FeeType.FIXED) {
            return value;
        }
        return totalAmount.multiply(value);
    }
}
