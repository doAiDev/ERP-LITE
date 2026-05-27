package com.erp.accessory.service;

import com.erp.accessory.dto.request.ProductRequest;
import com.erp.accessory.dto.request.VariantRequest;
import com.erp.accessory.entity.Product;
import com.erp.accessory.entity.ProductVariant;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public Map<String, Object> getProducts(String category, String brand, Boolean active, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        List<Product> items = productMapper.findProducts(category, brand, active, offset, size);
        int total = productMapper.countProducts(category, brand, active);
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Product getProductById(Integer productId) {
        Product product = productMapper.findProductById(productId);
        if (product == null) throw new CustomException(ErrorCode.NOT_FOUND, "상품 ID: " + productId);
        product.setVariants(productMapper.findVariantsByProductId(productId));
        return product;
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setActive(true);
        productMapper.insertProduct(product);

        if (request.getVariants() != null) {
            for (ProductRequest.VariantRequest vr : request.getVariants()) {
                ProductVariant variant = new ProductVariant();
                variant.setProductId(product.getProductId());
                variant.setSku(vr.getSku());
                variant.setColor(vr.getColor());
                variant.setSize(vr.getSize());
                variant.setMaterial(vr.getMaterial());
                variant.setActive(true);
                productMapper.insertVariant(variant);
            }
        }

        return getProductById(product.getProductId());
    }

    @Transactional
    public Product updateProduct(Integer productId, ProductRequest request) {
        Product product = productMapper.findProductById(productId);
        if (product == null) throw new CustomException(ErrorCode.NOT_FOUND, "상품 ID: " + productId);
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        if (request.getActive() != null) product.setActive(request.getActive());
        productMapper.updateProduct(product);
        return getProductById(productId);
    }

    @Transactional
    public ProductVariant addVariant(Integer productId, VariantRequest request) {
        if (productMapper.findProductById(productId) == null)
            throw new CustomException(ErrorCode.NOT_FOUND, "상품 ID: " + productId);
        ProductVariant variant = new ProductVariant();
        variant.setProductId(productId);
        variant.setSku(request.getSku());
        variant.setColor(request.getColor());
        variant.setSize(request.getSize());
        variant.setMaterial(request.getMaterial());
        variant.setActive(true);
        productMapper.insertVariant(variant);
        return productMapper.findVariantById(variant.getVariantId());
    }

    @Transactional
    public ProductVariant updateVariant(Integer variantId, VariantRequest request) {
        ProductVariant variant = productMapper.findVariantById(variantId);
        if (variant == null) throw new CustomException(ErrorCode.NOT_FOUND, "옵션 ID: " + variantId);
        variant.setSku(request.getSku());
        variant.setColor(request.getColor());
        variant.setSize(request.getSize());
        variant.setMaterial(request.getMaterial());
        if (request.getActive() != null) variant.setActive(request.getActive());
        productMapper.updateVariant(variant);
        return productMapper.findVariantById(variantId);
    }
}
