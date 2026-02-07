package com.paysplit.api.business;

import com.paysplit.api.converter.SettlementExecuteConverter;
import com.paysplit.api.dto.settlement.request.SettlementExecuteRequest;
import com.paysplit.api.dto.settlement.response.SettlementExecuteResponse;
import com.paysplit.api.dto.settlement.result.SettlementItemResult;
import com.paysplit.api.service.PaymentService;
import com.paysplit.api.service.SettlementPolicyService;
import com.paysplit.api.service.SettlementService;
import com.paysplit.common.annotation.Business;
import com.paysplit.common.error.payment.PaymentErrorCode;
import com.paysplit.common.error.payment.PaymentException;
import com.paysplit.db.domain.Payment;
import com.paysplit.db.domain.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@Transactional
@RequiredArgsConstructor
public class SettlementExecuteBusiness {
    private final SettlementService settlementService;
    private final PaymentService paymentService;
    private final SettlementPolicyService settlementPolicyService;
    private final SettlementExecuteConverter settlementExecuteConverter;

    public SettlementExecuteResponse execute(SettlementExecuteRequest request) {
        // 1. 결제 정보 조회 (유효성 확인)
        Payment payment = paymentService.getById(request.getPaymentId());

        if (!payment.isPayable()) {
            throw new PaymentException(PaymentErrorCode.INVALID_PAYMENT_STATE);
        }

        // 2. Settlement 엔티티 생성
        Settlement settlement = settlementService.createSettlement(payment);

        // 3. 정산 정책 적용 (금액 분배 계산)
        List<SettlementItemResult> results = settlementPolicyService.calculate(settlement);

        // 4. SettlementItem 생성 및 저장
        settlementService.createSettlementItems(settlement, results);

        // 5. 응답 DTO 변환 및 반환
        settlementService.completeSettlement(settlement);

        return settlementExecuteConverter.toResponse(settlement);
    }
}
