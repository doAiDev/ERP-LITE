package com.erp.accessory.mapper;

import com.erp.accessory.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PurchaseMapper {
    List<PurchaseOrder> findOrders(@Param("status") String status,
                                    @Param("supplierId") Integer supplierId,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    int countOrders(@Param("status") String status, @Param("supplierId") Integer supplierId);
    PurchaseOrder findById(@Param("orderId") int orderId);
    List<PurchaseOrderItem> findItemsByOrderId(@Param("orderId") int orderId);
    void insertOrder(PurchaseOrder order);
    void insertOrderItem(PurchaseOrderItem item);
    void updateStatus(@Param("orderId") int orderId, @Param("status") String status);
    void insertReceipt(GoodsReceipt receipt);
    void insertReceiptItem(GoodsReceiptItem item);
    void addReceivedQty(@Param("orderId") int orderId, @Param("variantId") int variantId, @Param("qty") int qty);
    int countPendingItems(@Param("orderId") int orderId);
    void incrementInventory(@Param("storeId") int storeId, @Param("variantId") int variantId, @Param("qty") int qty);
    List<Supplier> findAllSuppliers();
}
