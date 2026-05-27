package com.erp.accessory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 상품 응답 DTO
 * - variants: 상품 단위 목록 포함
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer productId;      // 상품 ID
    private String productName;     // 상품명
    private String category;        // 카테고리
    private String brand;           // 브랜드
    private String description;     // 설명
    private BigDecimal costPrice;   // 원가
    private BigDecimal salePrice;   // 판매가
    private Boolean isActive;       // 활성화 여부
    private LocalDateTime createdAt;
    private List<VariantDto> variants; // 상품 단위 목록

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantDto {
        private Integer variantId;     // 단위 ID
        private String sku;            // SKU
        private String color;          // 색상
        private String sizeInfo;       // 사이즈
        private BigDecimal extraPrice; // 추가 금액
        private Boolean isActive;      // 활성화 여부
    }
}
