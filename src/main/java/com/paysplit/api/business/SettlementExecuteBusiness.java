package com.paysplit.api.business;

import com.paysplit.api.dto.settlement.request.SettlementExecuteRequest;
import com.paysplit.api.dto.settlement.response.SettlementExecuteResponse;
import com.paysplit.api.service.PaymentService;
import com.paysplit.api.service.SettlementService;
import com.paysplit.common.annotation.Business;
import com.paysplit.common.error.payment.PaymentErrorCode;
import com.paysplit.common.error.payment.PaymentException;
import com.paysplit.db.domain.Payment;
import com.paysplit.db.domain.Settlement;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class SettlementExecuteBusiness {
    private final SettlementService settlementService;
    private final PaymentService paymentService;

    public SettlementExecuteResponse execute(SettlementExecuteRequest request) {
        // 1. 결제 정보 조회 (유효성 확인)
        Payment payment = paymentService.getById(request.getPaymentId());

        if (!payment.isPayable()) {
            throw new PaymentException(PaymentErrorCode.INVALID_PAYMENT_STATE);
        }

        // 2. Settlement 엔티티 생성
        Settlement settlement = settlementService.createSettlement(payment);

        // 3. 정산 정책 적용 (금액 분배 계산)
        // 4. SettlementItem 생성 및 저장
        // 5. 응답 DTO 변환 및 반환

        return null;
    }
}
