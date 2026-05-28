package com.erp.accessory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의 - 표준 에러 응답 {code, message, detail}
 */
@Getter
public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_002", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "DATA_001", "데이터를 찾을 수 없습니다."),
    DUPLICATE_ENTRY(HttpStatus.CONFLICT, "DATA_002", "이미 존재하는 데이터입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "REQ_001", "잘못된 요청입니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INV_001", "재고가 부족합니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "ORD_001", "현재 상태에서 허용되지 않는 작업입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_001", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
