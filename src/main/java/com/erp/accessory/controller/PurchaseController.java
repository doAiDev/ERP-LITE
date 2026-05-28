package com.erp.accessory.controller;

import com.erp.accessory.dto.request.GoodsReceiptRequest;
import com.erp.accessory.dto.request.PurchaseOrderRequest;
import com.erp.accessory.entity.PurchaseOrder;
import com.erp.accessory.entity.Supplier;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.security.UserPrincipal;
import com.erp.accessory.service.PurchaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@Tag(name = "발주", description = "발주/입고 관리 API")
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer supplierId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(purchaseService.getOrders(status, supplierId, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> get(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseService.getOrderById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseOrder>> create(
            @Valid @RequestBody PurchaseOrderRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(purchaseService.createOrder(req, principal.getStaffId())));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable int id, @RequestParam String status) {
        purchaseService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/receive")
    public ResponseEntity<ApiResponse<Void>> receive(
            @Valid @RequestBody GoodsReceiptRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        purchaseService.receiveGoods(req, principal.getStaffId());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/suppliers")
    public ResponseEntity<ApiResponse<List<Supplier>>> suppliers() {
        return ResponseEntity.ok(ApiResponse.success(purchaseService.getSuppliers()));
    }
}
