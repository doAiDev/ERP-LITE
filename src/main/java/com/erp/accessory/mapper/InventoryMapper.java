package com.erp.accessory.mapper;

import com.erp.accessory.entity.StoreInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InventoryMapper {

    List<StoreInventory> findInventory(@Param("storeId") Integer storeId,
                                       @Param("category") String category,
                                       @Param("lowStockOnly") Boolean lowStockOnly,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    int countInventory(@Param("storeId") Integer storeId,
                       @Param("category") String category,
                       @Param("lowStockOnly") Boolean lowStockOnly);

    List<StoreInventory> findLowStock(@Param("storeId") Integer storeId);

    StoreInventory findByStoreAndVariant(@Param("storeId") Integer storeId,
                                         @Param("variantId") Integer variantId);

    void setQuantity(@Param("storeId") Integer storeId,
                     @Param("variantId") Integer variantId,
                     @Param("quantity") Integer quantity);

    void adjustQuantity(@Param("storeId") Integer storeId,
                        @Param("variantId") Integer variantId,
                        @Param("delta") Integer delta);

    void upsertInventory(StoreInventory inventory);
}
