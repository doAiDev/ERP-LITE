package com.erp.accessory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 로그인 요청 DTO */
@Data
public class LoginRequest {
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
}
