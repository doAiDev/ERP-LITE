package com.erp.accessory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 직원 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {
    private Integer staffId;    // 직원 ID
    private Integer storeId;    // 소속 점포 ID
    private String storeName;   // 소속 점포명
    private String name;        // 직원명
    private String position;    // 직위
    private String phone;       // 연락처
    private String email;       // 이메일
    private String role;        // 권한
    private LocalDate hireDate; // 입사일
    private Boolean isActive;   // 재직 여부
    private LocalDateTime createdAt;
}
