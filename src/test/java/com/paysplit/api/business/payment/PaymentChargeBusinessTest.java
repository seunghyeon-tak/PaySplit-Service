package com.paysplit.api.business.payment;

import com.paysplit.api.business.PaymentChargeBusiness;
import com.paysplit.api.service.*;
import com.paysplit.common.client.TossPaymentClient;
import com.paysplit.common.client.dto.TossBillingChargeResponse;
import com.paysplit.db.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentChargeBusinessTest {
    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private PartyMemberService partyMemberService;

    @Mock
    private BillingKeyService billingKeyService;

    @Mock
    private TossPaymentClient tossPaymentClient;

    @Mock
    private PaymentService paymentService;

    @Mock
    private SettlementPolicyService settlementPolicyService;

    @InjectMocks
    private PaymentChargeBusiness paymentChargeBusiness;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList())
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("정상 결제 흐름")
    void charge_success() {
        // given
        Subscription subscription = mock(Subscription.class);
        PartyMember partyMember = mock(PartyMember.class);
        Party party = mock(Party.class);
        SubscriptionPlan plan = mock(SubscriptionPlan.class);
        SettlementPolicy policy = mock(SettlementPolicy.class);
        BillingKey billingKey = mock(BillingKey.class);
        User user = mock(User.class);
        TossBillingChargeResponse chargeResponse = mock(TossBillingChargeResponse.class);

        when(subscriptionService.getActiveSubscriptions(anyInt(), anyInt())).thenReturn(List.of(subscription)).thenReturn(Collections.emptyList());
        when(subscription.getParty()).thenReturn(party);
        when(subscription.getPlan()).thenReturn(plan);
        when(plan.getPrice()).thenReturn(BigDecimal.valueOf(13500));
        when(plan.getPolicy()).thenReturn(policy);
        when(plan.getName()).thenReturn("넷플릭스 스탠다드");
        when(party.getLeaderId()).thenReturn(1L);
        when(partyMemberService.getActiveMembers(any())).thenReturn(List.of(partyMember));
        when(partyMember.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(2L);
        when(user.getEmail()).thenReturn("test@test.com");
        when(user.getName()).thenReturn("test");
        when(settlementPolicyService.calculateLeaderDiscount(any(), any())).thenReturn(BigDecimal.valueOf(1000));
        when(billingKeyService.getByUser(user)).thenReturn(billingKey);
        when(billingKey.getBillingKey()).thenReturn("billingKey");
        when(billingKey.getCustomerKey()).thenReturn("customerKey");
        when(tossPaymentClient.chargeBilling(any(), any(), any(), any(), any(), any(), any())).thenReturn(chargeResponse);

        // when
        paymentChargeBusiness.charge();

        // then
        verify(tossPaymentClient).chargeBilling(any(), any(), any(), any(), any(), any(), any());
        verify(paymentService).save(any(), any(), any(), any());
    }

    @Test
    @DisplayName("멤버 없을 때 건너뜀")
    void charge_skip_empty_members() {
        // given
        Subscription subscription = mock(Subscription.class);
        Party party = mock(Party.class);

        when(subscriptionService.getActiveSubscriptions(anyInt(), anyInt())).thenReturn(List.of(subscription)).thenReturn(Collections.emptyList());
        when(subscription.getParty()).thenReturn(party);
        when(partyMemberService.getActiveMembers(any())).thenReturn(Collections.emptyList());

        // when
        paymentChargeBusiness.charge();

        // tjem
        verify(tossPaymentClient, never()).chargeBilling(any(), any(), any(), any(), any(), any(), any());
        verify(paymentService, never()).save(any(), any(), any(), any());
    }

    @Test
    @DisplayName("결제 실패 시 saveFailed 호출")
    void charge_fail_saveFailed() {
        // given
        Subscription subscription = mock(Subscription.class);
        Party party = mock(Party.class);
        SubscriptionPlan plan = mock(SubscriptionPlan.class);
        SettlementPolicy policy = mock(SettlementPolicy.class);
        PartyMember member = mock(PartyMember.class);
        User user = mock(User.class);
        BillingKey billingKey = mock(BillingKey.class);

        when(subscriptionService.getActiveSubscriptions(anyInt(), anyInt())).thenReturn(List.of(subscription)).thenReturn(Collections.emptyList());
        when(subscription.getParty()).thenReturn(party);
        when(subscription.getPlan()).thenReturn(plan);
        when(plan.getPrice()).thenReturn(BigDecimal.valueOf(13500));
        when(plan.getPolicy()).thenReturn(policy);
        when(plan.getName()).thenReturn("넷플릭스 스탠다드");
        when(party.getLeaderId()).thenReturn(1L);
        when(partyMemberService.getActiveMembers(any())).thenReturn(List.of(member));
        when(member.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(2L);
        when(user.getEmail()).thenReturn("test@test.com");
        when(user.getName()).thenReturn("test");
        when(settlementPolicyService.calculateLeaderDiscount(any(), any())).thenReturn(BigDecimal.valueOf(1000));
        when(billingKeyService.getByUser(user)).thenReturn(billingKey);
        when(billingKey.getBillingKey()).thenReturn("billingKey");
        when(billingKey.getCustomerKey()).thenReturn("customerKey");
        when(tossPaymentClient.chargeBilling(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("결제 실패"));

        // when
        paymentChargeBusiness.charge();

        // then
        verify(paymentService, never()).save(any(), any(), any(), any());
        verify(paymentService).saveFailed(any(), any(), any());
    }
}
