package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 공급업체 엔티티
 * - suppliers 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private Integer supplierId;    // 공급업체 ID (PK)
    private String supplierName;   // 업체명
    private String contactName;    // 담당자명
    private String phone;          // 전화번호
    private String email;          // 이메일
    private String address;        // 주소
    private String notes;          // 비고
    private Boolean isActive;      // 활성화 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
