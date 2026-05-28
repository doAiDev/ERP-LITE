package com.erp.accessory.controller;

import com.erp.accessory.dto.request.InventoryAdjustRequest;
import com.erp.accessory.dto.request.TransferRequest;
import com.erp.accessory.entity.StoreInventory;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Tag(name = "재고", description = "점포별 재고 관리 API")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "재고 목록")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventory(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean lowStockOnly,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            inventoryService.getInventory(storeId, category, lowStockOnly, page, size)));
    }

    @Operation(summary = "부족 재고 목록")
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<StoreInventory>>> getLowStock(
            @RequestParam(required = false) Integer storeId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getLowStock(storeId)));
    }

    @Operation(summary = "재고 조정 (ADMIN/MANAGER)")
    @PatchMapping("/adjust")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<StoreInventory>> adjustStock(
            @Valid @RequestBody InventoryAdjustRequest request) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.adjustStock(request)));
    }

    @Operation(summary = "점포간 재고 이동 (ADMIN/MANAGER)")
    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<Void>> transferStock(
            @Valid @RequestBody TransferRequest request) {
        inventoryService.transferStock(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
