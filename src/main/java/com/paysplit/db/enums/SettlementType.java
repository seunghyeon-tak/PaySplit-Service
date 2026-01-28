package com.paysplit.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettlementType {
    NORMAL("자동 정산"),
    ADJUSTMENT("수동 정산"),
    REVERSAL("취소 정산")
    ;

    private final String description;
}
