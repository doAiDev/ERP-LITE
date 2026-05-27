package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 직원 엔티티
 * - staff 테이블 매핑
 * - role 필드: ADMIN/MANAGER/STAFF
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    private Integer staffId;    // 직원 ID (PK)
    private Integer storeId;    // 소속 점포 ID (FK)
    private String name;        // 직원명
    private String position;    // 직위
    private String phone;       // 연락처
    private String email;       // 이메일
    private String role;        // 권한 (ADMIN/MANAGER/STAFF)
    private LocalDate hireDate; // 입사일
    private Boolean isActive;   // 재직 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // JOIN 결과를 위한 필드
    private String storeName;   // 소속 점포명 (stores 테이블 JOIN)
}
