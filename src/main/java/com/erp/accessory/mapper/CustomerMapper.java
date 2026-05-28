package com.erp.accessory.mapper;

import com.erp.accessory.entity.Customer;
import com.erp.accessory.entity.PointTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CustomerMapper {
    List<Customer> findCustomers(@Param("search") String search,
                                  @Param("grade") String grade,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);
    int countCustomers(@Param("search") String search, @Param("grade") String grade);
    Customer findById(@Param("customerId") int customerId);
    void insertCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void updateAfterSale(@Param("customerId") int customerId, @Param("amount") BigDecimal amount);
    void updatePoints(@Param("customerId") int customerId, @Param("delta") int delta);
    void insertPointTransaction(PointTransaction tx);
}
