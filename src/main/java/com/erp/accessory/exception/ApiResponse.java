package com.erp.accessory.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * 공통 API 응답 래퍼 - {code, message, detail, data}
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final String code;
    private final String message;
    private final String detail;
    private final T data;

    private ApiResponse(String code, String message, String detail, T data) {
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "성공", null, data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("SUCCESS", "성공", null, null);
    }

    public static <T> ApiResponse<T> error(String code, String message, String detail) {
        return new ApiResponse<>(code, message, detail, null);
    }
}
