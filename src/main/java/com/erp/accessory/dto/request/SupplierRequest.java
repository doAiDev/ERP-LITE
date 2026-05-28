package com.erp.accessory.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class SupplierRequest {
    @NotBlank @Size(max = 100) private String supplierName;
    @Size(max = 50)  private String contactName;
    @Size(max = 20)  private String phone;
    @Size(max = 100) private String email;
    @Size(max = 200) private String address;
    @Size(max = 100) private String paymentTerms;
    private Integer leadTimeDays;
    private Boolean isActive;
}
