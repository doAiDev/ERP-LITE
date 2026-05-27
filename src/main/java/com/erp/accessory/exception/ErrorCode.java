package com.erp.accessory.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 열거형
 * - 모든 비즈니스 예외를 코드화
 * - HTTP 상태코드와 메시지를 함께 정의
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ========================
    // 인증/인가 에러
    // ========================
    /** 인증되지 않은 요청 */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "로그인이 필요합니다"),
    /** 유효하지 않은 토큰 */
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 인증 토큰입니다"),
    /** 만료된 토큰 */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_003", "인증 토큰이 만료되었습니다"),
    /** 접근 권한 없음 */
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_004", "접근 권한이 없습니다"),
    /** 비밀번호 불일치 */
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_005", "아이디 또는 비밀번호가 잘못되었습니다"),

    // ========================
    // 리소스 에러
    // ========================
    /** 요청한 리소스를 찾을 수 없음 */
    NOT_FOUND(HttpStatus.NOT_FOUND, "RES_001", "요청한 리소스를 찾을 수 없습니다"),
    /** 점포를 찾을 수 없음 */
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_002", "점포를 찾을 수 없습니다"),
    /** 직원을 찾을 수 없음 */
    STAFF_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_003", "직원을 찾을 수 없습니다"),
    /** 상품을 찾을 수 없음 */
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_004", "상품을 찾을 수 없습니다"),
    /** 고객을 찾을 수 없음 */
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_005", "고객을 찾을 수 없습니다"),
    /** 발주를 찾을 수 없음 */
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_006", "발주를 찾을 수 없습니다"),
    /** 상품 단위 (옵션)을 찾을 수 없음 */
    VARIANT_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_007", "상품 단위를 찾을 수 없습니다"),
    /** 공급업체를 찾을 수 없음 */
    SUPPLIER_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_008", "공급업체를 찾을 수 없습니다"),

    // ========================
    // 입력값 검증 엔러
    // ========================
    /** 잘못된 요청 파라미터 */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "VAL_001", "잘못된 요청 파라미터입니다"),
    /** 필수 파라미터 누락 */
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "VAL_002", "필수 파라미터가 누락되었습니다"),
    /** 중복 데이터 엔트리 */
    DUPLICATE_ENTRY(HttpStatus.CONFLICT, "VAL_003", "이미 존재하는 데이터입니다"),
    /** 중복 사용자명 */
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "VAL_004", "이미 사용 중인 아이디입니다"),
    /** 중복 전화번호 */
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "VAL_005", "이미 등록된 전화번호입니다"),
    /** 중복 SKU */
    DUPLICATE_SKU(HttpStatus.CONFLICT, "VAL_006", "이미 사용 중인 SKU입니다"),

    // ========================
    // 재고 에러
    // ========================
    /** 재고 부족 */
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INV_001", "재고가 부족합니다"),
    /** 동일 점포 재고 이동 불가 */
    SAME_STORE_TRANSFER(HttpStatus.BAD_REQUEST, "INV_002", "동일 점포 간 재고 이동은 불가능합니다"),

    // ========================
    // 포인트 에러
    // ========================
    /** 포인트 부족 */
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "PNT_001", "포인트가 부족합니다"),

    // ========================
    // 판매 엔러
    // ========================
    /** 이미 환불 완료된 판매 */
    ALREADY_REFUNDED(HttpStatus.BAD_REQUEST, "SALE_001", "이미 환불 처리된 판매입니다"),
    /** 판매를 찾을 수 없음 */
    SALE_NOT_FOUND(HttpStatus.NOT_FOUND, "SALE_002", "판매 내역을 찾을 수 없습니다"),

    // ========================
    // 서버 엔러
    // ========================
    /** 내부 서버 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_001", "내부 서버 오류가 발생했습니다"),
    /** 외부 서비스 오류 */
    EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "SYS_002", "외부 서비스 연동에 실패했습니다");

    private final HttpStatus httpStatus; // HTTP 상태코드
    private final String code;           // 엔러 코드 (클라이언트에 전달)
    private final String message;        // 사용자 친화적인 메시지
}
