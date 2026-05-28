package com.erp.accessory.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 전역 예외 처리기 - 표준 에러 응답 {code, message, detail} 통일
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustom(CustomException e) {
        log.warn("[CustomException] {} - {}", e.getErrorCode().getCode(), e.getDetail());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage(), e.getDetail()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("REQ_001", "입력값 검증 실패", detail));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("AUTH_002", "접근 권한이 없습니다.", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception e) {
        log.error("[UnhandledException]", e);
        return ResponseEntity.status(500)
                .body(ApiResponse.error("SYS_001", "서버 내부 오류", e.getMessage()));
    }
}
