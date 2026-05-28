package com.erp.accessory.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.util.List;

@Getter @Setter
public class GoodsReceiptRequest {
    @NotNull private Integer orderId;
    private String notes;
    @NotEmpty private List<ReceiptItemRequest> items;

    @Getter @Setter
    public static class ReceiptItemRequest {
        @NotNull private Integer variantId;
        @NotNull @Min(1) private Integer receivedQty;
        private String condition = "normal";
    }
}
