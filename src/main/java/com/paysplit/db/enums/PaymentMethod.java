package com.paysplit.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CARD("카드 결제"),
    TRANSFER("계좌 이체"),
    CASH("현금"),
    ETC("기타")
    ;

    private final String description;
}
