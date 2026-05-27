package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 상품 단위 (옵션) 엔티티
 * - product_variants 테이블 매핑
 * - productName: products 테이블 JOIN용 필드
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    private Integer variantId;     // 단위 ID (PK)
    private Integer productId;     // 상품 ID (FK)
    private String sku;            // 재고 관리 코드
    private String color;          // 색상
    private String sizeInfo;       // 사이즈 정보
    private BigDecimal extraPrice; // 추가 금액
    private Boolean isActive;      // 활성화 여부
    private LocalDateTime createdAt;

    // JOIN 결과를 위한 필드
    private String productName;    // 상품명 (products JOIN)
    private String category;       // 카테고리 (products JOIN)
    private BigDecimal salePrice;  // 판매가 (products JOIN)
    private BigDecimal costPrice;  // 원가 (products JOIN)
}
