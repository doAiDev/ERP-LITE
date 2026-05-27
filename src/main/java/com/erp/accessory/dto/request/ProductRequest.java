package com.erp.accessory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 상품 등록/수정 요청 DTO
 * - variants: 상품 단위 목록 포함
 */
@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "상품명을 입력해 주세요")
    @Size(max = 100, message = "상품명은 100자 이내로 입력해 주세요")
    private String productName;   // 상품명

    @NotBlank(message = "카테고리를 입력해 주세요")
    @Pattern(regexp = "팔찌|목걸이|귀걸이|반지|기타",
             message = "카테고리는 팔찌, 목걸이, 귀걸이, 반지, 기타 중 하나여야 합니다")
    private String category;       // 카테고리

    @Size(max = 50)
    private String brand;          // 브랜드

    @Size(max = 500)
    private String description;    // 상품 설명

    @NotNull(message = "원가를 입력해 주세요")
    @DecimalMin(value = "0", message = "원가는 0 이상이어야 합니다")
    private BigDecimal costPrice;  // 원가

    @NotNull(message = "판매가를 입력해 주세요")
    @DecimalMin(value = "0", message = "판매가는 0 이상이어야 합니다")
    private BigDecimal salePrice;  // 판매가

    @Valid
    private List<VariantRequest> variants; // 상품 단위 목록

    /**
     * 상품 단위 등록 요청 (Product 내부 클래스)
     */
    @Getter
    @Setter
    public static class VariantRequest {

        @NotBlank(message = "SKU를 입력해 주세요")
        @Size(max = 50, message = "SKU는 50자 이내로 입력해 주세요")
        private String sku;        // SKU 코드

        @Size(max = 50)
        private String color;      // 색상

        @Size(max = 50)
        private String sizeInfo;   // 사이즈 정보

        private BigDecimal extraPrice = BigDecimal.ZERO; // 추가 금액
    }
}
