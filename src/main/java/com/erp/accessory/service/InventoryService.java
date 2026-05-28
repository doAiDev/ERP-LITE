package com.erp.accessory.service;

import com.erp.accessory.dto.request.InventoryAdjustRequest;
import com.erp.accessory.dto.request.TransferRequest;
import com.erp.accessory.entity.StoreInventory;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryMapper inventoryMapper;

    public Map<String, Object> getInventory(Integer storeId, String category, Boolean lowStockOnly, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        List<StoreInventory> items = inventoryMapper.findInventory(storeId, category, lowStockOnly, offset, size);
        int total = inventoryMapper.countInventory(storeId, category, lowStockOnly);
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public List<StoreInventory> getLowStock(Integer storeId) {
        return inventoryMapper.findLowStock(storeId);
    }

    @Transactional
    public StoreInventory adjustStock(InventoryAdjustRequest request) {
        StoreInventory inv = inventoryMapper.findByStoreAndVariant(request.getStoreId(), request.getVariantId());
        if (inv == null) throw new CustomException(ErrorCode.NOT_FOUND, "해당 재고를 찾을 수 없습니다.");
        inventoryMapper.setQuantity(request.getStoreId(), request.getVariantId(), request.getQuantity());
        return inventoryMapper.findByStoreAndVariant(request.getStoreId(), request.getVariantId());
    }

    @Transactional
    public void transferStock(TransferRequest request) {
        StoreInventory from = inventoryMapper.findByStoreAndVariant(request.getFromStoreId(), request.getVariantId());
        if (from == null || from.getQuantity() < request.getQuantity())
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK,
                "출고 점포 재고: " + (from != null ? from.getQuantity() : 0));
        inventoryMapper.adjustQuantity(request.getFromStoreId(), request.getVariantId(), -request.getQuantity());
        StoreInventory to = inventoryMapper.findByStoreAndVariant(request.getToStoreId(), request.getVariantId());
        if (to == null) {
            StoreInventory newInv = new StoreInventory();
            newInv.setStoreId(request.getToStoreId());
            newInv.setVariantId(request.getVariantId());
            newInv.setQuantity(request.getQuantity());
            newInv.setMinQuantity(0);
            inventoryMapper.upsertInventory(newInv);
        } else {
            inventoryMapper.adjustQuantity(request.getToStoreId(), request.getVariantId(), request.getQuantity());
        }
    }
}
