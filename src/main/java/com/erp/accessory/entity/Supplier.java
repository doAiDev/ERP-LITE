package com.erp.accessory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private Integer supplierId;
    private String supplierName;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String paymentTerms;
    private Integer leadTimeDays;
    private Boolean isActive;
}
