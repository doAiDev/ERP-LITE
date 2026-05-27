package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 상품 엔티티
 * - products 테이블 매핑
 * - variants: 상품 단위 목록 (세부조회 시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer productId;     // 상품 ID (PK)
    private String productName;    // 상품명
    private String category;       // 카테고리 (팔찌/목걸이/귀걸이/반지/기타)
    private String brand;          // 브랜드
    private String description;    // 상품 설명
    private BigDecimal costPrice;  // 원가
    private BigDecimal salePrice;  // 판매가
    private Boolean isActive;      // 활성화 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 세부 조회 시 상품 단위 목록
    private List<ProductVariant> variants;
}
