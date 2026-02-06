package com.paysplit.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements ErrorCode {
    INVALID_REQUEST(
            "COMMON_001",
            "잘못된 요청입니다",
            HttpStatus.BAD_REQUEST
    ),

    UNAUTHORIZED(
            "COMMON_002",
            "인증이 필요합니다",
            HttpStatus.UNAUTHORIZED
    ),

    FORBIDDEN(
            "COMMON_003",
            "접근 권한이 없습니다",
            HttpStatus.FORBIDDEN
    ),

    INTERNAL_SERVER_ERROR(
            "COMMON_999",
            "서버 내부 오류가 발생했습니다",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String code;
    private final String message;
    private final HttpStatus status;

    CommonErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
