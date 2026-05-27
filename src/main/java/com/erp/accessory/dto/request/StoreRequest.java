package com.erp.accessory.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

/** 점포 등록/수정 요청 DTO */
@Data
public class StoreRequest {
    @NotBlank(message = "점포명을 입력해 주세요.")
    private String name;
    private String address;
    private String phone;
    private LocalDate openedAt;
}
