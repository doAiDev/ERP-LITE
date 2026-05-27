package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 재고 이동 엔티티
 * - inventory_transfers 테이블 매핀
 * - 점포 간 재고 이동 내역
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransfer {
    private Integer transferId;    // 이동 ID (PK)
    private Integer fromStoreId;   // 출발 점포 ID
    private Integer toStoreId;     // 도착 점포 ID
    private Integer variantId;     // 상품 단위 ID
    private Integer quantity;      // 이동 수량
    private Integer transferredBy; // 처리 직원 ID
    private LocalDateTime transferredAt; // 이동 일시
    private String notes;          // 비고

    // JOIN 결과를 위한 필드
    private String fromStoreName;  // 출발 점포명
    private String toStoreName;    // 도착 점포명
    private String productName;    // 상품명
    private String sku;            // SKU
    private String staffName;      // 처리 직원명
}
