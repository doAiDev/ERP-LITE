package com.erp.accessory.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 점포 엔티티 */
@Data
public class Store {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private LocalDate openedAt;
    private boolean active;
    private LocalDateTime createdAt;
}
