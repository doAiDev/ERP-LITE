package com.erp.accessory.controller;

import com.erp.accessory.dto.request.CustomerRequest;
import com.erp.accessory.entity.Customer;
import com.erp.accessory.exception.ApiResponse;
import com.erp.accessory.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

@Tag(name = "고객", description = "고객 관리 API")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String grade,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomers(search, grade, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> get(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> create(@Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(ApiResponse.success(customerService.createCustomer(req)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> update(@PathVariable int id, @Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(ApiResponse.success(customerService.updateCustomer(id, req)));
    }
}
