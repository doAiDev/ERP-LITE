package com.erp.accessory.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통 API 응답 래퍼
 * - 모든 API 응답에 사용
 * - success: 성공/실패 여부
 * - code: 엔러 코드 (성공 시 null)
 * - message: 응답 메시지
 * - data: 응답 데이터 (제네릭)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON 응답에 제외
public class ApiResponse<T> {

    private boolean success;  // 성공 여부
    private String code;      // 엔러 코드
    private String message;   // 응답 메시지
    private T data;           // 응답 데이터
    private String detail;    // 상세 에러 메시지 (nullable)

    /**
     * 성공 응답 생성 (data 없음)
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
            .success(true)
            .message("요청이 성공적으로 처리되었습니다")
            .build();
    }

    /**
     * 성공 응답 생성 (data 포함)
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message("요청이 성공적으로 처리되었습니다")
            .data(data)
            .build();
    }

    /**
     * 성공 응답 생성 (data + message)
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }

    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> error(String code, String message, String detail) {
        return ApiResponse.<T>builder()
            .success(false)
            .code(code)
            .message(message)
            .detail(detail)
            .build();
    }
}
