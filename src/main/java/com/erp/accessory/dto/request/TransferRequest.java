package com.erp.accessory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 재고 이동 요청 DTO
 * - 점포 간 재고 이동 시 사용
 */
@Getter
@Setter
public class TransferRequest {

    @NotNull(message = "출발 점포를 선택해 주세요")
    private Integer fromStoreId; // 출발 점포 ID

    @NotNull(message = "도착 점포를 선택해 주세요")
    private Integer toStoreId;   // 도착 점포 ID

    @NotNull(message = "상품 단위를 선택해 주세요")
    private Integer variantId;   // 상품 단위 ID

    @NotNull(message = "수량을 입력해 주세요")
    @Min(value = 1, message = "이동 수량은 1 이상이어야 합니다")
    private Integer quantity;    // 이동 수량

    private String notes;        // 이동 사유 (선택)
}
