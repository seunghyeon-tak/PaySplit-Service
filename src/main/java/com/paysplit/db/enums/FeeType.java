package com.paysplit.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeeType {
    FIXED("금액"),
    RATE("비율")
    ;

    private final String description;
}
