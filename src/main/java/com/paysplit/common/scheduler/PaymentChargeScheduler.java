package com.paysplit.common.scheduler;

import com.paysplit.api.business.PaymentChargeBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentChargeScheduler {
    private final PaymentChargeBusiness paymentChargeBusiness;

    @Scheduled(cron = "0 0 0 1 * *")
    public void processPaymentCharge() {
        log.info("자동 결제 청구 스케줄러 실행");
        paymentChargeBusiness.charge();
    }
}
