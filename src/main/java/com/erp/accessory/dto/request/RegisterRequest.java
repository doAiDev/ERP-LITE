package com.erp.accessory.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "이름을 입력해 주세요")
    @Size(max = 50)
    private String name;

    @Size(max = 20)
    private String phone;

    @NotNull(message = "점포를 선택해 주세요")
    private Integer storeId;

    @NotBlank(message = "아이디를 입력해 주세요")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(min = 6, max = 50)
    private String password;
}
