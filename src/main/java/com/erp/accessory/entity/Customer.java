package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 고객 엔티티
 * - customers 테이블 매핀
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Integer customerId;      // 고객 ID (PK)
    private String name;             // 고객명
    private String phone;            // 전화번호
    private String email;            // 이메일
    private LocalDate birthDate;     // 생년월일
    private String gender;           // 성별
    private String grade;            // 등급 (bronze/silver/gold/vip)
    private Integer pointBalance;    // 포인트 잔액
    private BigDecimal totalPurchase; // 누적 구매금액
    private String memo;             // 메모
    private Boolean isActive;        // 활성화 여부
    private LocalDateTime joinedAt;  // 가입일
    private LocalDateTime updatedAt;
}
