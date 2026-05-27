package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 인증 사용자 엔티티
 * - auth_users 테이블 매핑
 * - storeId, role은 staff 테이블 JOIN 결과
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    private Integer userId;        // 사용자 ID (PK)
    private Integer staffId;       // 직원 ID (FK)
    private String username;       // 로그인 아이디
    private String passwordHash;   // BCrypt 해시
    private boolean isActive;      // 활성화 여부
    private LocalDateTime lastLoginAt; // 마지막 로그인
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // staff 테이블 JOIN 결과를 담는 필드
    private Integer storeId;       // 소속 점포 ID
    private String role;           // 권한 (ADMIN/MANAGER/STAFF)
    private String staffName;      // 직원명
}
