package com.erp.accessory.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 직원 엔티티 */
@Data
public class Staff {
    private Integer staffId;
    private Integer storeId;
    private String name;
    private String phone;
    private String role;       // admin | manager | staff
    private boolean active;
    private LocalDateTime createdAt;
    // JOIN 조회 시 코드
    private String storeName;
}
