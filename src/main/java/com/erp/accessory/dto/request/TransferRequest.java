package com.erp.accessory.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    @NotNull(message = "출발 점포를 선택해 주세요")
    private Integer fromStoreId;

    @NotNull(message = "도착 점포를 선택해 주세요")
    private Integer toStoreId;

    @NotNull(message = "상품 단위를 선택해 주세요")
    private Integer variantId;

    @NotNull(message = "수량을 입력해 주세요")
    @Min(value = 1)
    private Integer quantity;

    private String notes;
}
