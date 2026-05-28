package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 일일 정산 엔티티
 * - daily_settlements 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySettlement {
    private Integer settlementId;        // 정산 ID (PK)
    private Integer storeId;             // 점포 ID (FK)
    private LocalDate settlementDate;    // 정산일
    private Integer totalSalesCount;     // 판매 건수
    private BigDecimal totalAmount;      // 총 매출
    private BigDecimal cashAmount;       // 현금 매출
    private BigDecimal cardAmount;       // 카드 매출
    private BigDecimal transferAmount;   // 계좌이체 매출
    private BigDecimal refundAmount;     // 환불 금액
    private BigDecimal netAmount;        // 순매출
    private LocalDateTime createdAt;

    // JOIN 결과를 위한 필드
    private String storeName;            // 점포명
}
