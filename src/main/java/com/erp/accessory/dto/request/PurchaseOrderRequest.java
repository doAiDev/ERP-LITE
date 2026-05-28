package com.erp.accessory.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class PurchaseOrderRequest {
    @NotNull private Integer supplierId;
    @NotNull private Integer storeId;
    private LocalDate expectedDate;
    private String notes;
    @NotEmpty private List<OrderItemRequest> items;

    @Getter @Setter
    public static class OrderItemRequest {
        @NotNull private Integer variantId;
        @NotNull @Min(1) private Integer orderedQty;
        @NotNull private BigDecimal unitCost;
    }
}
