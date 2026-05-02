package com.paysplit.common.client.dto;

import lombok.Data;

@Data
public class TossBillingChargeResponse {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String method;
    private Integer totalAmount;
    private String currency;
    private TossCardInfo card;

    @Data
    public static class TossCardInfo {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private String cardType;
        private String ownerType;
        private String approveNo;
        private Integer amount;
    }
}
