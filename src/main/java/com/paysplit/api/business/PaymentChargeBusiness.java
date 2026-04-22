package com.paysplit.api.business;

import com.paysplit.api.service.*;
import com.paysplit.common.annotation.Business;
import com.paysplit.common.client.TossPaymentClient;
import com.paysplit.common.client.dto.TossBillingChargeResponse;
import com.paysplit.db.domain.BillingKey;
import com.paysplit.db.domain.PartyMember;
import com.paysplit.db.domain.SettlementPolicy;
import com.paysplit.db.domain.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Slf4j
@Business
@RequiredArgsConstructor
@Transactional
public class PaymentChargeBusiness {
    @Value("${payment.charge.batch-size:100}")
    private int batchSize;

    private final SubscriptionService subscriptionService;
    private final PartyMemberService partyMemberService;
    private final BillingKeyService billingKeyService;
    private final TossPaymentClient tossPaymentClient;
    private final PaymentService paymentService;
    private final SettlementPolicyService settlementPolicyService;

    public void charge() {
        int page = 0;

        // active 구독 파티 조회
        List<Subscription> subscriptions;

        do {
            subscriptions = subscriptionService.getActiveSubscriptions(page, batchSize);

            for (Subscription sub : subscriptions) {
                // 파티 멤버 조회
                List<PartyMember> members = partyMemberService.getActiveMembers(sub.getParty().getId());
                if (members.isEmpty()) {
                    log.warn("파티 멤버가 없습니다 - partyId: {}", sub.getParty().getId());
                    continue;
                }
                log.info("결제 청구 시작 - partyId: {}, memberCount: {}", sub.getParty().getId(), members.size());

                // 금액 계산
                BigDecimal planPrice = sub.getPlan().getPrice();
                int memberCount = members.size();

                // 파티장 할인 적용
                SettlementPolicy policy = sub.getPlan().getPolicy();
                BigDecimal leaderDiscount = settlementPolicyService.calculateLeaderDiscount(policy, planPrice);
                BigDecimal memberAmount = planPrice.divide(BigDecimal.valueOf(memberCount), RoundingMode.DOWN);
                BigDecimal leaderAmount = memberAmount.subtract(leaderDiscount);
                if (leaderAmount.compareTo(BigDecimal.ZERO) < 0) {
                    leaderAmount = BigDecimal.ZERO;
                }

                // 각 멤버의 빌링키 조회
                for (PartyMember member : members) {
                    BigDecimal amount = member.getUser().getId().equals(sub.getParty().getLeaderId()) ? leaderAmount : memberAmount;

                    // 빌링키 조회
                    BillingKey billingKey = billingKeyService.getByUser(member.getUser());

                    // 결제 청구 (toss 결제 청구)
                    String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
                    String orderName = sub.getPlan().getName() + " 구독료";

                    try {
                        TossBillingChargeResponse chargeResponse = tossPaymentClient.chargeBilling(
                                billingKey.getBillingKey(),
                                billingKey.getCustomerKey(),
                                amount,
                                orderId,
                                orderName,
                                member.getUser().getEmail(),
                                member.getUser().getName()
                        );

                        // 결제 결과 저장
                        paymentService.save(member, sub, chargeResponse, amount);

                        log.info("결제 청구 완료 - userId: {}, amount: {}", member.getUser().getId(), amount);
                    } catch (Exception e) {
                        log.error("결제 청구 실패 - userId: {}, partyId: {}", member.getUser().getId(), sub.getParty().getId(), e);

                        // 결제 실패 저장
                        paymentService.saveFailed(member, sub, amount);
                    }
                }
            }

            page++;
        } while (subscriptions.size() == batchSize);


    }
}
