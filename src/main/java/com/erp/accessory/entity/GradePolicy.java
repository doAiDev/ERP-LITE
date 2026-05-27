package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 등급 정책 엔티티
 * - grade_policies 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradePolicy {
    private Integer policyId;        // 정책 ID (PK)
    private String gradeName;        // 등급명 (bronze/silver/gold/vip)
    private BigDecimal minPurchase;  // 최소 누적 구매금액
    private BigDecimal pointRate;    // 포인트 적립률 (%)
    private BigDecimal discountRate; // 할인율 (%)
    private String description;      // 설명
    private LocalDateTime updatedAt;
}
