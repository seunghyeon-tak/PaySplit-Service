package com.paysplit.api.dto.settlement.result;

import com.paysplit.db.enums.ReceiverType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementItemResult {
    private ReceiverType receiverType;
    private Long receiverId;
    private BigDecimal amount;
}
