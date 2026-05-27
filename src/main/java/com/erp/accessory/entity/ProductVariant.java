package com.erp.accessory.entity;

import lombok.Data;

/** 상품 옵션 엔티티 (color/size/material) */
@Data
public class ProductVariant {
    private Integer variantId;
    private Integer productId;
    private String color;
    private String size;
    private String material;
    private String sku;
    private boolean active;
    // JOIN 코드
    private String productName;
    private String category;
    private String brand;
}
