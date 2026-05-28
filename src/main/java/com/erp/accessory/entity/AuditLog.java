package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 감사 로그 엔티티
 * - audit_logs 테이블 매핀
 * - AuditAspect에서 자동 생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    private Integer logId;         // 로그 ID (PK)
    private Integer userId;        // 사용자 ID (FK, nullable)
    private Integer staffId;       // 직원 ID (FK, nullable)
    private String action;         // 행위 (CREATE/UPDATE/DELETE)
    private String targetTable;    // 대상 테이블
    private String targetId;       // 대상 레코드 ID
    private String oldValue;       // 변경 전 값 (JSON)
    private String newValue;       // 변경 후 값 (JSON)
    private String ipAddress;      // 클라이언트 IP
    private String userAgent;      // 브라우저 정보
    private LocalDateTime createdAt;
}
