package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 발주 엔티티
 * - purchase_orders 테이블 매핀
 * - items: 발주 항목 목록 (세부조회 시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    private Integer orderId;         // 발주 ID (PK)
    private Integer supplierId;      // 공급업체 ID (FK)
    private Integer storeId;         // 대상 점포 ID (FK)
    private Integer orderedBy;       // 발주 담당자 ID
    private LocalDateTime orderDate; // 발주 일시
    private LocalDate expectedDate;  // 입고 예정일
    private String status;           // 상태 (PENDING/PARTIAL/RECEIVED/CANCELLED)
    private BigDecimal totalAmount;  // 발주 금액
    private String notes;            // 비고
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // JOIN 결과를 위한 필드
    private String supplierName;     // 공급업체명
    private String storeName;        // 점포명
    private String staffName;        // 발주 담당자명

    // 세부 조회 시 항목 목록
    private List<PurchaseOrderItem> items;
}
