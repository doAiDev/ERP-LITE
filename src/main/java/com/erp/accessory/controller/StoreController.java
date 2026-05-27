package com.erp.accessory.controller;

import com.erp.accessory.dto.request.StaffRequest;
import com.erp.accessory.dto.request.StoreRequest;
import com.erp.accessory.entity.Staff;
import com.erp.accessory.entity.Store;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 점포 · 직원 관리 컨트롤러
 */
@Tag(name = "점포/직원", description = "점포 등록 · 직원 관리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "점포 목록")
    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<List<Store>>> getStores() {
        return ResponseEntity.ok(ApiResponse.success(storeService.getAllStores()));
    }

    @Operation(summary = "점포 단건")
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<Store>> getStore(@PathVariable Integer storeId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getStoreById(storeId)));
    }

    @Operation(summary = "점포 등록 (ADMIN 전용)")
    @PostMapping("/stores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Store>> createStore(@Valid @RequestBody StoreRequest req) {
        return ResponseEntity.ok(ApiResponse.success(storeService.createStore(req)));
    }

    @Operation(summary = "점포 수정")
    @PutMapping("/stores/{storeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateStore(
            @PathVariable Integer storeId, @Valid @RequestBody StoreRequest req) {
        storeService.updateStore(storeId, req);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "직원 목록")
    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<List<Staff>>> getStaff(
            @RequestParam(required = false) Integer storeId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getStaff(storeId)));
    }

    @Operation(summary = "직원 등록")
    @PostMapping("/staff")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<Staff>> createStaff(@Valid @RequestBody StaffRequest req) {
        return ResponseEntity.ok(ApiResponse.success(storeService.createStaff(req)));
    }

    @Operation(summary = "비밀번호 변경")
    @PutMapping("/staff/{staffId}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Integer staffId,
            @RequestBody Map<String, String> body) {
        storeService.changePassword(staffId, body.get("password"));
        return ResponseEntity.ok(ApiResponse.success());
    }
}
