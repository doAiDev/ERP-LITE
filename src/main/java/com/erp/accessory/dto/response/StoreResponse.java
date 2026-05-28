package com.erp.accessory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 점포 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
    private Integer storeId;       // 점포 ID
    private String storeName;      // 점포명
    private String address;        // 주소
    private String phone;          // 전화번호
    private String managerName;    // 담당 매니저명
    private Boolean isActive;      // 활성화 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
