package com.paysplit.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiverType {
    MEMBER_PAYMENT("파티원 결제"),
    LEADER_PAYMENT("파티장 결제"),
    PLATFORM_SUPPORT("플랫폼 지원");

    private final String description;
}
