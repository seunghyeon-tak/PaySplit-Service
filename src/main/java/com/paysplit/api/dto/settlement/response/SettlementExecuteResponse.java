package com.paysplit.api.dto.settlement.response;

import com.paysplit.db.enums.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementExecuteResponse {
    private Long settlementId;
    private SettlementStatus status;
}
