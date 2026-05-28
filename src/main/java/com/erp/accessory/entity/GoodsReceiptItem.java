package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 입고 항목 엔티티
 * - goods_receipt_items 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItem {
    private Integer receiptItemId;  // 항목 ID (PK)
    private Integer receiptId;      // 입고 ID (FK)
    private Integer orderItemId;    // 발주 항목 ID (FK)
    private Integer variantId;      // 상품 단위 ID (FK)
    private Integer receivedQty;    // 실제 입고 수량
    private String condition;       // 상태 (GOOD/DAMAGED/MISSING)
    private String notes;           // 비고

    // JOIN 결과를 위한 필드
    private String productName;     // 상품명
    private String sku;             // SKU
    private String color;           // 색상
    private String sizeInfo;        // 사이즈
}
