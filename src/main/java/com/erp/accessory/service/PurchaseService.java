package com.erp.accessory.service;

import com.erp.accessory.dto.request.GoodsReceiptRequest;
import com.erp.accessory.dto.request.PurchaseOrderRequest;
import com.erp.accessory.entity.*;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseMapper purchaseMapper;

    public Map<String, Object> getOrders(String status, Integer supplierId, int page, int size) {
        int offset = (page - 1) * size;
        List<PurchaseOrder> items = purchaseMapper.findOrders(status, supplierId, offset, size);
        int total = purchaseMapper.countOrders(status, supplierId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public PurchaseOrder getOrderById(int orderId) {
        PurchaseOrder order = purchaseMapper.findById(orderId);
        if (order == null) throw new CustomException(ErrorCode.NOT_FOUND, "발주서를 찾을 수 없습니다.");
        order.setItems(purchaseMapper.findItemsByOrderId(orderId));
        return order;
    }

    @Transactional
    public PurchaseOrder createOrder(PurchaseOrderRequest req, int staffId) {
        BigDecimal total = req.getItems().stream()
                .map(i -> i.getUnitCost().multiply(new BigDecimal(i.getOrderedQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplierId(req.getSupplierId());
        order.setStoreId(req.getStoreId());
        order.setExpectedDate(req.getExpectedDate());
        order.setNotes(req.getNotes());
        order.setTotalAmount(total);
        order.setCreatedBy(staffId);
        purchaseMapper.insertOrder(order);
        for (PurchaseOrderRequest.OrderItemRequest item : req.getItems()) {
            PurchaseOrderItem poi = new PurchaseOrderItem();
            poi.setOrderId(order.getOrderId());
            poi.setVariantId(item.getVariantId());
            poi.setOrderedQty(item.getOrderedQty());
            poi.setUnitCost(item.getUnitCost());
            purchaseMapper.insertOrderItem(poi);
        }
        return getOrderById(order.getOrderId());
    }

    public void updateStatus(int orderId, String status) {
        PurchaseOrder order = getOrderById(orderId);
        if ("completed".equals(order.getStatus()) || "cancelled".equals(order.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_STATUS, "완료/취소된 발주서는 상태 변경이 불가합니다.");
        }
        purchaseMapper.updateStatus(orderId, status);
    }

    @Transactional
    public void receiveGoods(GoodsReceiptRequest req, int staffId) {
        PurchaseOrder order = getOrderById(req.getOrderId());
        if ("cancelled".equals(order.getStatus()) || "completed".equals(order.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_STATUS, "입고 처리할 수 없는 상태입니다.");
        }
        GoodsReceipt receipt = new GoodsReceipt();
        receipt.setOrderId(req.getOrderId());
        receipt.setReceivedBy(staffId);
        receipt.setNotes(req.getNotes());
        purchaseMapper.insertReceipt(receipt);
        for (GoodsReceiptRequest.ReceiptItemRequest item : req.getItems()) {
            GoodsReceiptItem gri = new GoodsReceiptItem();
            gri.setReceiptId(receipt.getReceiptId());
            gri.setVariantId(item.getVariantId());
            gri.setReceivedQty(item.getReceivedQty());
            gri.setCondition(item.getCondition() != null ? item.getCondition() : "normal");
            purchaseMapper.insertReceiptItem(gri);
            purchaseMapper.incrementInventory(order.getStoreId(), item.getVariantId(), item.getReceivedQty());
            purchaseMapper.addReceivedQty(req.getOrderId(), item.getVariantId(), item.getReceivedQty());
        }
        int pending = purchaseMapper.countPendingItems(req.getOrderId());
        purchaseMapper.updateStatus(req.getOrderId(), pending == 0 ? "completed" : "partial");
    }

    public List<Supplier> getSuppliers() {
        return purchaseMapper.findAllSuppliers();
    }
}
