package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 포인트 내역 엔티티
 * - point_transactions 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointTransaction {
    private Integer txId;           // 내역 ID (PK)
    private Integer customerId;     // 고객 ID (FK)
    private Integer amount;         // 포인트 변동량 (양수: 적립, 음수: 사용)
    private Integer balanceAfter;   // 변동 후 잔액
    private String txType;          // 유형 (EARN/USE/EXPIRE/MANUAL)
    private String description;     // 설명
    private Integer saleId;         // 관련 판매 ID (nullable)
    private Integer createdBy;      // 등록 직원 ID
    private LocalDateTime createdAt;

    // JOIN 결과를 위한 필드
    private String customerName;    // 고객명
    private String staffName;       // 직원명
}
