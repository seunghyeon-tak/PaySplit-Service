package com.paysplit.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiverType {
    LEADER("파티장"),
    PLATFORM("플랫폼");

    private final String description;
}
