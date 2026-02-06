package com.paysplit.api.dto.settlement.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementExecuteRequest {
    private Long paymentId;
    private Integer totalAmount;
}
