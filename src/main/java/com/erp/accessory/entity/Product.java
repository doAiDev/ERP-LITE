package com.erp.accessory.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** 상품 엔티티 */
@Data
public class Product {
    private Integer productId;
    private String name;
    private String category;
    private String brand;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 로드 시 옵션 목록
    private List<ProductVariant> variants;
}
