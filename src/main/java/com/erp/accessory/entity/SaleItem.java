package com.erp.accessory.entity;

import lombok.Data;
import java.math.BigDecimal;

/** 매출 품목 엔티티 */
@Data
public class SaleItem {
    private Integer itemId;
    private Integer saleId;
    private Integer variantId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountRate;
    // JOIN 코드
    private String productName;
    private String sku;
}
