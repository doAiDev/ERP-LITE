package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 점포별 재고 엔티티
 * - store_inventory 테이블 매핀
 * - 상품/단위 정보 JOIN용 필드 포함
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInventory {
    private Integer inventoryId;   // 재고 ID (PK)
    private Integer storeId;       // 점포 ID (FK)
    private Integer variantId;     // 상품 단위 ID (FK)
    private Integer quantity;      // 현재 재고 수량
    private Integer lowStockAlert; // 재고 부족 알림 기준
    private LocalDateTime updatedAt;

    // 상품 정보 JOIN용 필드 (product_variants + products JOIN)
    private String sku;            // SKU 코드
    private String productName;    // 상핈명
    private String category;       // 카테고리
    private String color;          // 색상
    private String sizeInfo;       // 사이즈
    private BigDecimal salePrice;  // 판매가
    private BigDecimal costPrice;  // 원가
    private String storeName;      // 점포명 (stores JOIN)

    // 계산 필드
    private Boolean isLowStock;    // 재고 부족 여부
}
