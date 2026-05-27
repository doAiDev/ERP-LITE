package com.erp.accessory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 직원 등록 요청 DTO (계정 동시 생성) */
@Data
public class StaffRequest {
    @NotNull(message = "점포 ID를 입력해 주세요.")
    private Integer storeId;

    @NotBlank(message = "직원명을 입력해 주세요.")
    private String name;

    private String phone;

    @NotBlank(message = "역할을 입력해 주세요.")
    private String role;  // admin | manager | staff

    @NotBlank(message = "아이디를 입력해 주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
}
