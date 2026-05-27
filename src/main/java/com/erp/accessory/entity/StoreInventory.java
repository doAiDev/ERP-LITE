package com.erp.accessory.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 점포별 재고 엔티티 */
@Data
public class StoreInventory {
    private Integer inventoryId;
    private Integer storeId;
    private Integer variantId;
    private Integer quantity;
    private Integer minQuantity;
    private LocalDateTime updatedAt;
    // JOIN 코드
    private String storeName;
    private String productName;
    private String category;
    private String brand;
    private String color;
    private String size;
    private String material;
    private String sku;
    private boolean lowStock;  // quantity < min_quantity
}
