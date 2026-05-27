package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 판매 엔티티
 * - sales 테이블 매핀
 * - items: 판매 항목 목록 (세부조회 시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private Integer saleId;           // 판매 ID (PK)
    private Integer storeId;          // 점포 ID (FK)
    private Integer staffId;          // 직원 ID (FK)
    private Integer customerId;       // 고객 ID (FK, nullable)
    private BigDecimal totalAmount;   // 합계 금액
    private BigDecimal discountAmount; // 할인 금액
    private Integer pointUsed;        // 사용 포인트
    private Integer pointEarned;      // 적립 포인트
    private BigDecimal finalAmount;   // 최종 결제금액
    private String paymentMethod;     // 결제수단 (card/cash/transfer)
    private String source;            // 입력 경로 (manual/pos)
    private String status;            // 상태 (COMPLETED/REFUNDED/PARTIAL_REFUND)
    private String notes;             // 비고
    private LocalDateTime soldAt;     // 판매 일시
    private LocalDateTime createdAt;

    // JOIN 결과를 위한 필드
    private String storeName;         // 점포명
    private String staffName;         // 직원명
    private String customerName;      // 고객명

    // 세부 조회 시 판매 항목 목록
    private List<SaleItem> items;
}
