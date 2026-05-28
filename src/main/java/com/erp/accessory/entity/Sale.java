package com.erp.accessory.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** 매출 엔티티 */
@Data
public class Sale {
    private Integer saleId;
    private Integer storeId;
    private Integer staffId;
    private Integer customerId;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String paymentMethod;  // cash | card | kakao_pay | naver_pay
    private String source;         // manual | pos
    private boolean refunded;
    // JOIN 코드
    private String storeName;
    private String staffName;
    private String customerName;
    private List<SaleItem> items;
}
