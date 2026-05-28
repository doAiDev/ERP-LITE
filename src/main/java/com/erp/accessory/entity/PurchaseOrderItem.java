package com.erp.accessory.entity;

import lombok.Data;
import java.math.BigDecimal;

/** 발주 품목 엔티티 */
@Data
public class PurchaseOrderItem {
    private Integer orderItemId;
    private Integer orderId;
    private Integer variantId;
    private Integer orderedQty;
    private Integer receivedQty;
    private BigDecimal unitCost;
    // JOIN + 계산 코드
    private String productName;
    private String sku;
    private Double receivedRate;  // received_qty / ordered_qty * 100
}
