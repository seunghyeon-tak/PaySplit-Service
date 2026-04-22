package com.paysplit.common.error.billing;

import lombok.Getter;

@Getter
public class BillingKeyException extends RuntimeException {
    private final BillingKeyErrorCode errorCode;

    public BillingKeyException(BillingKeyErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
