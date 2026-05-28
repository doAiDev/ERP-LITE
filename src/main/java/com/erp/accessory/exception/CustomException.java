package com.erp.accessory.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 예외 - ErrorCode + 세부 메시지 포함
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detail;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = null;
    }

    public CustomException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
