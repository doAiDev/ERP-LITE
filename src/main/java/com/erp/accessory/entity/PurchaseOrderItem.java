package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 발주 항목 엔티티
 * - purchase_order_items 테이블 매핀
 * - receivedRate: 입고율 계산용 필드
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem {
    private Integer orderItemId;   // 항목 ID (PK)
    private Integer orderId;       // 발주 ID (FK)
    private Integer variantId;     // 상품 단위 ID (FK)
    private Integer orderedQty;    // 발주 수량
    private Integer receivedQty;   // 입고 완료 수량
    private BigDecimal unitCost;   // 발주 단가
    private BigDecimal subtotal;   // 소계

    // JOIN 결과를 위한 필드
    private String productName;    // 상품명
    private String sku;            // SKU
    private String color;          // 색상
    private String sizeInfo;       // 사이즈

    // 계산 필드
    private BigDecimal receivedRate; // 입고율 (receivedQty / orderedQty * 100)
}
