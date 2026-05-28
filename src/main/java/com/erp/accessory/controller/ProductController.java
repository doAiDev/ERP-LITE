package com.erp.accessory.controller;

import com.erp.accessory.dto.request.ProductRequest;
import com.erp.accessory.dto.request.VariantRequest;
import com.erp.accessory.entity.Product;
import com.erp.accessory.entity.ProductVariant;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Tag(name = "상품", description = "상품 등록 · 옵션 관리 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 (페이징)")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProducts(category, brand, active, page, size)));
    }

    @Operation(summary = "상품 단건 (옵션 포함)")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(productId)));
    }

    @Operation(summary = "상품 등록 (ADMIN/MANAGER)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(request)));
    }

    @Operation(summary = "상품 수정 (ADMIN/MANAGER)")
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(productId, request)));
    }

    @Operation(summary = "옵션 추가 (ADMIN/MANAGER)")
    @PostMapping("/{productId}/variants")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<ProductVariant>> addVariant(
            @PathVariable Integer productId,
            @Valid @RequestBody VariantRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.addVariant(productId, request)));
    }

    @Operation(summary = "옵션 수정 (ADMIN/MANAGER)")
    @PutMapping("/variants/{variantId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<ProductVariant>> updateVariant(
            @PathVariable Integer variantId,
            @Valid @RequestBody VariantRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateVariant(variantId, request)));
    }
}
