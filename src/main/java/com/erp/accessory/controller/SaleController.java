package com.erp.accessory.controller;

import com.erp.accessory.dto.request.SaleRequest;
import com.erp.accessory.entity.Sale;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.security.UserPrincipal;
import com.erp.accessory.service.SaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

@Tag(name = "매출", description = "매출 관리 API")
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(saleService.getSales(storeId, startDate, endDate, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sale>> get(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.success(saleService.getSaleById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Sale>> create(
            @Valid @RequestBody SaleRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(saleService.createSale(req, principal.getStaffId())));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<Void>> refund(@PathVariable int id) {
        saleService.refundSale(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/today-stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> todayStats(
            @RequestParam(required = false) Integer storeId) {
        return ResponseEntity.ok(ApiResponse.success(saleService.getTodayStats(storeId)));
    }
}
