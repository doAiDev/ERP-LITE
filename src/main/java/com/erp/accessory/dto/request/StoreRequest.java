package com.erp.accessory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 점포 등록/수정 요청 DTO
 */
@Getter
@Setter
public class StoreRequest {

    @NotBlank(message = "점포명을 입력해 주세요")
    @Size(max = 100, message = "점포명은 100자 이내로 입력해 주세요")
    private String storeName;    // 점포명

    @Size(max = 200, message = "주소는 200자 이내로 입력해 주세요")
    private String address;      // 주소

    @Size(max = 20, message = "전화번호는 20자 이내로 입력해 주세요")
    private String phone;        // 전화번호

    @Size(max = 50, message = "매니저명은 50자 이내로 입력해 주세요")
    private String managerName;  // 담당 매니저명
}
