package com.erp.accessory.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuthUser {
    private Integer userId;
    private Integer staffId;
    private String username;
    private String hashedPassword;
    private LocalDateTime lastLogin;
    private boolean active;
    private String role;
    private Integer storeId;
    private String staffName;
    private String storeName;
}
