package com.erp.accessory.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAdjustRequest {

    @NotNull(message = "점포를 선택해 주세요")
    private Integer storeId;

    @NotNull(message = "상품 단위를 선택해 주세요")
    private Integer variantId;

    @NotNull(message = "수량을 입력해 주세요")
    @Min(value = 0)
    private Integer quantity;

    @NotBlank(message = "조정 사유를 입력해 주세요")
    private String reason;
}
