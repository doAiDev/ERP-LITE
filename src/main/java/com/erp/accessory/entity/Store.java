package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 점포 엔티티
 * - stores 테이블 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private Integer storeId;       // 점포 ID (PK)
    private String storeName;      // 점포명
    private String address;        // 주소
    private String phone;          // 전화번호
    private String managerName;    // 담당 매니저명
    private Boolean isActive;      // 활성화 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
