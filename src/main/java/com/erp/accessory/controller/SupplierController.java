package com.erp.accessory.controller;

import com.erp.accessory.dto.request.SupplierRequest;
import com.erp.accessory.entity.Supplier;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.service.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

@Tag(name = "공급업체", description = "공급업체 관리 API")
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.getSuppliers(search, isActive, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> get(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Supplier>> create(@Valid @RequestBody SupplierRequest req) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.createSupplier(req)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> update(
            @PathVariable int id, @Valid @RequestBody SupplierRequest req) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.updateSupplier(id, req)));
    }
}
