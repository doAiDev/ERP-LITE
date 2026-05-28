package com.erp.accessory.mapper;

import com.erp.accessory.entity.Sale;
import com.erp.accessory.entity.SaleItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface SaleMapper {
    List<Sale> findSales(@Param("storeId") Integer storeId,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate,
                          @Param("offset") int offset,
                          @Param("limit") int limit);
    int countSales(@Param("storeId") Integer storeId,
                   @Param("startDate") LocalDate startDate,
                   @Param("endDate") LocalDate endDate);
    Sale findById(@Param("saleId") int saleId);
    List<SaleItem> findItemsBySaleId(@Param("saleId") int saleId);
    void insertSale(Sale sale);
    void insertSaleItem(SaleItem item);
    void markRefunded(@Param("saleId") int saleId);
    Map<String, Object> getTodayStats(@Param("storeId") Integer storeId);
}
