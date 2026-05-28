package com.erp.accessory.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class SaleRequest {
    @NotNull private Integer storeId;
    private Integer customerId;
    @NotBlank private String paymentMethod;
    private BigDecimal discountAmount;
    @NotEmpty private List<SaleItemRequest> items;

    @Getter @Setter
    public static class SaleItemRequest {
        @NotNull private Integer variantId;
        @NotNull @Min(1) private Integer quantity;
        @NotNull private BigDecimal unitPrice;
        private BigDecimal discountRate;
    }
}
