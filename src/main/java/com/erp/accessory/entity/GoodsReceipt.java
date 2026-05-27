package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 입고 엔티티
 * - goods_receipts 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {
    private Integer receiptId;       // 입고 ID (PK)
    private Integer orderId;         // 발주 ID (FK)
    private Integer receivedBy;      // 입고 담당자 ID
    private LocalDateTime receivedAt; // 입고 일시
    private String notes;            // 비고

    // JOIN 결과를 위한 필드
    private String staffName;        // 입고 담당자명
    private String supplierName;     // 공급업체명

    // 입고 항목 목록
    private List<GoodsReceiptItem> items;
}
