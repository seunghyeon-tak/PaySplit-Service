package com.paysplit.common.error.billing;

import com.paysplit.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BillingKeyErrorCode implements ErrorCode {
    BILLING_KEY_NOT_FOUND(
            "BILLING_001",
            "빌링키가 존재하지 않습니다",
            HttpStatus.NOT_FOUND
    ),
    BILLING_KEY_USER_NOT_FOUND(
            "BILLING_002",
            "빌링키를 가진 사용자가 존재하지 않습니다",
            HttpStatus.NOT_FOUND
    ),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    BillingKeyErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
