package com.erp.accessory.controller;

import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.mapper.StoreMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자", description = "계정 승인/거절 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StoreMapper storeMapper;

    @Operation(summary = "승인 대기 계정 목록")
    @GetMapping("/users/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AuthUser>>> getPendingUsers() {
        return ResponseEntity.ok(ApiResponse.success(storeMapper.findPendingUsers()));
    }

    @Operation(summary = "계정 승인")
    @PostMapping("/users/{userId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approveUser(@PathVariable Integer userId) {
        storeMapper.approveUser(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "계정 거절")
    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectUser(@PathVariable Integer userId) {
        storeMapper.deletePendingUser(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
