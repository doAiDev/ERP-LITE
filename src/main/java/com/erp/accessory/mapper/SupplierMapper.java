package com.erp.accessory.mapper;

import com.erp.accessory.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SupplierMapper {
    List<Supplier> findSuppliers(@Param("search") String search,
                                  @Param("isActive") Boolean isActive,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);
    int countSuppliers(@Param("search") String search, @Param("isActive") Boolean isActive);
    Supplier findById(@Param("supplierId") int supplierId);
    void insertSupplier(Supplier supplier);
    void updateSupplier(Supplier supplier);
}
