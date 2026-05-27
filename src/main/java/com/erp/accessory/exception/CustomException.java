package com.erp.accessory.exception;

import lombok.Getter;

/**
 * 코드도화된 비즈니스 예외
 * - ErrorCode를 포함하여 일관된 에러 응답 제공
 * - GlobalExceptionHandler에서 일괄 처리
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detail; // 추가 상세 메시지 (nullable)

    /**
     * ErrorCode만으로 생성
     */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = null;
    }

    /**
     * ErrorCode + 상세 메시지로 생성
     */
    public CustomException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }

    /**
     * ErrorCode + 원인 예외로 생성
     */
    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.detail = cause.getMessage();
    }
}
