package com.erp.accessory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 재고 현황 응답 DTO
 * - isLowStock: 재고 부족 여부 포함
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Integer inventoryId;   // 재고 ID
    private Integer storeId;       // 점포 ID
    private String storeName;      // 점포명
    private Integer variantId;     // 상품 단위 ID
    private String sku;            // SKU
    private String productName;    // 상품명
    private String category;       // 카테고리
    private String color;          // 색상
    private String sizeInfo;       // 사이즈
    private BigDecimal salePrice;  // 판매가
    private BigDecimal costPrice;  // 원가
    private Integer quantity;      // 현재 재고 수량
    private Integer lowStockAlert; // 재고 부족 기준
    private Boolean isLowStock;    // 재고 부족 여부 (quantity <= lowStockAlert)
    private LocalDateTime updatedAt;
}
