package com.erp.accessory.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 직원 등록 요청 DTO
 * - 직원 정보 + 계정(auth_user) 동시 생성
 */
@Getter
@Setter
public class StaffRequest {

    @NotNull(message = "소속 점포를 선택해 주세요")
    private Integer storeId;     // 소속 점포 ID

    @NotBlank(message = "직원명을 입력해 주세요")
    @Size(max = 50, message = "직원명은 50자 이내로 입력해 주세요")
    private String name;         // 직원명

    @Size(max = 50)
    private String position;     // 직위 (점장/직원)

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식: 010-XXXX-XXXX")
    private String phone;        // 연락처

    @Email(message = "올바른 이메일 형식을 입력해 주세요")
    private String email;        // 이메일

    @NotNull(message = "권한을 선택해 주세요")
    @Pattern(regexp = "ADMIN|MANAGER|STAFF", message = "권한은 ADMIN, MANAGER, STAFF 중 하나여야 합니다")
    private String role;         // 권한

    // 로그인 계정 정보
    @NotBlank(message = "로그인 아이디를 입력해 주세요")
    @Size(min = 4, max = 50, message = "아이디는 4~50자여야 합니다")
    private String username;     // 로그인 아이디

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    private String password;     // 비밀번호 (평문)
}
