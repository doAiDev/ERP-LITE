package com.erp.accessory.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 발주서 엔티티 */
@Data
public class PurchaseOrder {
    private Integer orderId;
    private Integer supplierId;
    private Integer storeId;
    private LocalDateTime orderDate;
    private LocalDate expectedDate;
    private String status; // draft|sent|confirmed|partial|completed|cancelled
    private BigDecimal totalAmount;
    private String notes;
    private Integer createdBy;
    // JOIN 코드
    private String supplierName;
    private String storeName;
    private List<PurchaseOrderItem> items;
}
