package com.erp.accessory.service;

import com.erp.accessory.dto.request.SaleRequest;
import com.erp.accessory.entity.*;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleMapper saleMapper;
    private final InventoryMapper inventoryMapper;
    private final CustomerMapper customerMapper;

    public Map<String, Object> getSales(Integer storeId, String startDate, String endDate, int page, int size) {
        int offset = (page - 1) * size;
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end   = endDate   != null ? LocalDate.parse(endDate)   : null;
        List<Sale> items = saleMapper.findSales(storeId, start, end, offset, size);
        int total = saleMapper.countSales(storeId, start, end);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Sale getSaleById(int saleId) {
        Sale sale = saleMapper.findById(saleId);
        if (sale == null) throw new CustomException(ErrorCode.NOT_FOUND, "매출을 찾을 수 없습니다.");
        sale.setItems(saleMapper.findItemsBySaleId(saleId));
        return sale;
    }

    @Transactional
    public Sale createSale(SaleRequest req, int staffId) {
        BigDecimal discount = req.getDiscountAmount() != null ? req.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        for (SaleRequest.SaleItemRequest item : req.getItems()) {
            StoreInventory inv = inventoryMapper.findByStoreAndVariant(req.getStoreId(), item.getVariantId());
            if (inv == null || inv.getQuantity() < item.getQuantity()) {
                throw new CustomException(ErrorCode.INSUFFICIENT_STOCK, "재고가 부족합니다. variantId=" + item.getVariantId());
            }
            BigDecimal rate = item.getDiscountRate() != null ? item.getDiscountRate() : BigDecimal.ZERO;
            BigDecimal line = item.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()))
                    .multiply(BigDecimal.ONE.subtract(rate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)));
            total = total.add(line);
        }
        total = total.subtract(discount);

        Sale sale = new Sale();
        sale.setStoreId(req.getStoreId());
        sale.setStaffId(staffId);
        sale.setCustomerId(req.getCustomerId());
        sale.setTotalAmount(total);
        sale.setDiscountAmount(discount);
        sale.setPaymentMethod(req.getPaymentMethod());
        saleMapper.insertSale(sale);

        for (SaleRequest.SaleItemRequest item : req.getItems()) {
            SaleItem si = new SaleItem();
            si.setSaleId(sale.getSaleId());
            si.setVariantId(item.getVariantId());
            si.setQuantity(item.getQuantity());
            si.setUnitPrice(item.getUnitPrice());
            si.setDiscountRate(item.getDiscountRate() != null ? item.getDiscountRate() : BigDecimal.ZERO);
            saleMapper.insertSaleItem(si);
            inventoryMapper.adjustQuantity(req.getStoreId(), item.getVariantId(), -item.getQuantity());
        }

        if (req.getCustomerId() != null) {
            int earned = total.multiply(new BigDecimal("0.01")).intValue();
            customerMapper.updateAfterSale(req.getCustomerId(), total);
            if (earned > 0) {
                customerMapper.updatePoints(req.getCustomerId(), earned);
                Customer c = customerMapper.findById(req.getCustomerId());
                PointTransaction pt = new PointTransaction();
                pt.setCustomerId(req.getCustomerId());
                pt.setType("earn");
                pt.setAmount(earned);
                pt.setBalanceAfter(c.getPointBalance());
                pt.setDescription("매출 적립 (ID:" + sale.getSaleId() + ")");
                pt.setSaleId(sale.getSaleId());
                customerMapper.insertPointTransaction(pt);
            }
        }
        return getSaleById(sale.getSaleId());
    }

    @Transactional
    public void refundSale(int saleId) {
        Sale sale = getSaleById(saleId);
        if (sale.isRefunded()) throw new CustomException(ErrorCode.INVALID_STATUS, "이미 환불된 매출입니다.");
        saleMapper.markRefunded(saleId);
        for (SaleItem item : sale.getItems()) {
            inventoryMapper.adjustQuantity(sale.getStoreId(), item.getVariantId(), item.getQuantity());
        }
    }

    public Map<String, Object> getTodayStats(Integer storeId) {
        return saleMapper.getTodayStats(storeId);
    }
}
