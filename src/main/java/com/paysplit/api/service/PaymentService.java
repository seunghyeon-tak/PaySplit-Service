package com.paysplit.api.service;

import com.paysplit.common.client.dto.TossBillingChargeResponse;
import com.paysplit.common.error.payment.PaymentErrorCode;
import com.paysplit.common.error.payment.PaymentException;
import com.paysplit.db.domain.PartyMember;
import com.paysplit.db.domain.Payment;
import com.paysplit.db.domain.Subscription;
import com.paysplit.db.enums.PaymentStatus;
import com.paysplit.db.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.paysplit.common.error.payment.PaymentErrorCode.PAYMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    /*
     * 정산 실행 (중복 호출) 방지를 위해 비관락으로 결제 row를 잠금
     * */
    public Payment getByIdForUpdate(Long paymentId) {
        return paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new PaymentException(PAYMENT_NOT_FOUND));
    }

    public void settleIfNotSettled(Long paymentId) {
        int updated = paymentRepository.markSettledIfNotSettled(paymentId, LocalDateTime.now());

        if (updated == 0) {
            // 이미 정산됨
            throw new PaymentException(PaymentErrorCode.ALREADY_SETTLED);
        }
    }

    public Payment save(PartyMember member, Subscription subscription, TossBillingChargeResponse chargeResponse, BigDecimal amount) {
        Payment payment = Payment.builder()
                .amount(amount)
                .settlementPolicy(subscription.getPlan().getPolicy())
                .status(PaymentStatus.COMPLETED)
                .payerId(member.getUser().getId())
                .externalPaymentId(chargeResponse.getPaymentKey())
                .pgProvider("TOSS")
                .currency(chargeResponse.getCurrency())
                .orderId(chargeResponse.getOrderId())
                .approvedAt(LocalDateTime.parse(chargeResponse.getApprovedAt(), DateTimeFormatter.ISO_OFFSET_TIME))
                .build();

        return paymentRepository.save(payment);
    }

    public void saveFailed(PartyMember member, Subscription subscription, BigDecimal amount) {
        Payment payment = Payment.builder()
                .amount(amount)
                .settlementPolicy(subscription.getPlan().getPolicy())
                .status(PaymentStatus.FAILED)
                .payerId(member.getUser().getId())
                .pgProvider("TOSS")
                .currency("KRW")
                .build();

        paymentRepository.save(payment);
    }
}
