package com.erp.accessory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 재고 조정 요청 DTO
 * - 재고를 직접 증감/감소 시 사용
 */
@Getter
@Setter
public class InventoryAdjustRequest {

    @NotNull(message = "점포를 선택해 주세요")
    private Integer storeId;    // 점포 ID

    @NotNull(message = "상품 단위를 선택해 주세요")
    private Integer variantId;  // 상품 단위 ID

    @NotNull(message = "수량을 입력해 주세요")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다")
    private Integer quantity;   // 조정 후 재고 수량 (절대값)

    @NotBlank(message = "조정 사유를 입력해 주세요")
    private String reason;      // 조정 사유 (조정입고/파손/도난 등)
}
