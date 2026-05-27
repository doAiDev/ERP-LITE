package com.erp.accessory.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 고객 엔티티 */
@Data
public class Customer {
    private Integer customerId;
    private String name;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private String grade;  // bronze | silver | gold | vip
    private BigDecimal totalPurchaseAmount;
    private Integer visitCount;
    private Integer pointBalance;
    private LocalDateTime createdAt;
    private Integer createdStoreId;
}
