package com.erp.accessory.service;

import com.erp.accessory.dto.request.SupplierRequest;
import com.erp.accessory.entity.Supplier;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierMapper supplierMapper;

    public Map<String, Object> getSuppliers(String search, Boolean isActive, int page, int size) {
        int offset = (page - 1) * size;
        List<Supplier> items = supplierMapper.findSuppliers(search, isActive, offset, size);
        int total = supplierMapper.countSuppliers(search, isActive);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Supplier getById(int supplierId) {
        Supplier s = supplierMapper.findById(supplierId);
        if (s == null) throw new CustomException(ErrorCode.NOT_FOUND, "공급업체를 찾을 수 없습니다.");
        return s;
    }

    public Supplier createSupplier(SupplierRequest req) {
        Supplier s = new Supplier();
        s.setSupplierName(req.getSupplierName());
        s.setContactName(req.getContactName());
        s.setPhone(req.getPhone());
        s.setEmail(req.getEmail());
        s.setAddress(req.getAddress());
        s.setPaymentTerms(req.getPaymentTerms());
        s.setLeadTimeDays(req.getLeadTimeDays());
        s.setIsActive(req.getIsActive() != null ? req.getIsActive() : Boolean.TRUE);
        supplierMapper.insertSupplier(s);
        return supplierMapper.findById(s.getSupplierId());
    }

    public Supplier updateSupplier(int id, SupplierRequest req) {
        getById(id);
        Supplier s = new Supplier();
        s.setSupplierId(id);
        s.setSupplierName(req.getSupplierName());
        s.setContactName(req.getContactName());
        s.setPhone(req.getPhone());
        s.setEmail(req.getEmail());
        s.setAddress(req.getAddress());
        s.setPaymentTerms(req.getPaymentTerms());
        s.setLeadTimeDays(req.getLeadTimeDays());
        s.setIsActive(req.getIsActive() != null ? req.getIsActive() : Boolean.TRUE);
        supplierMapper.updateSupplier(s);
        return supplierMapper.findById(id);
    }
}
