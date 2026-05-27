package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 판매 항목 엔티티
 * - sale_items 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {
    private Integer itemId;        // 항목 ID (PK)
    private Integer saleId;        // 판매 ID (FK)
    private Integer variantId;     // 상품 단위 ID (FK)
    private Integer quantity;      // 판매 수량
    private BigDecimal unitPrice;  // 판매 단가
    private BigDecimal discount;   // 항목 할인
    private BigDecimal subtotal;   // 소계
    private Boolean isRefunded;    // 환불 여부

    // JOIN 결과를 위한 필드
    private String productName;    // 상품명
    private String sku;            // SKU
    private String color;          // 색상
    private String sizeInfo;       // 사이즈
}
