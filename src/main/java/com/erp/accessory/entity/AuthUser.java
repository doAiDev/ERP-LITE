package com.erp.accessory.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 인증 계정 엔티티 - staff JOIN 포함 */
@Data
public class AuthUser {
    private Integer userId;
    private Integer staffId;
    private String username;
    private String hashedPassword;
    private LocalDateTime lastLogin;
    private boolean active;
    // staff JOIN 필드
    private Integer storeId;
    private String role;
    private String staffName;
}
