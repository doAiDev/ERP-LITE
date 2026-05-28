package com.erp.accessory.service;

import com.erp.accessory.dto.request.CustomerRequest;
import com.erp.accessory.entity.Customer;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;

    public Map<String, Object> getCustomers(String search, String grade, int page, int size) {
        int offset = (page - 1) * size;
        List<Customer> items = customerMapper.findCustomers(search, grade, offset, size);
        int total = customerMapper.countCustomers(search, grade);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Customer getById(int customerId) {
        Customer c = customerMapper.findById(customerId);
        if (c == null) throw new CustomException(ErrorCode.NOT_FOUND, "고객을 찾을 수 없습니다.");
        return c;
    }

    public Customer createCustomer(CustomerRequest req) {
        Customer c = new Customer();
        c.setName(req.getName());
        c.setPhone(req.getPhone());
        c.setEmail(req.getEmail());
        c.setBirthDate(req.getBirthDate());
        c.setCreatedStoreId(req.getCreatedStoreId());
        customerMapper.insertCustomer(c);
        return customerMapper.findById(c.getCustomerId());
    }

    public Customer updateCustomer(int id, CustomerRequest req) {
        getById(id);
        Customer c = new Customer();
        c.setCustomerId(id);
        c.setName(req.getName());
        c.setPhone(req.getPhone());
        c.setEmail(req.getEmail());
        c.setBirthDate(req.getBirthDate());
        customerMapper.updateCustomer(c);
        return customerMapper.findById(id);
    }
}
