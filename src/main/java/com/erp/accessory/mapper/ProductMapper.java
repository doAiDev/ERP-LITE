package com.erp.accessory.mapper;

import com.erp.accessory.entity.Product;
import com.erp.accessory.entity.ProductVariant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findProducts(@Param("category") String category,
                               @Param("brand") String brand,
                               @Param("active") Boolean active,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    int countProducts(@Param("category") String category,
                      @Param("brand") String brand,
                      @Param("active") Boolean active);

    Product findProductById(@Param("productId") Integer productId);

    void insertProduct(Product product);

    void updateProduct(Product product);

    List<ProductVariant> findVariantsByProductId(@Param("productId") Integer productId);

    ProductVariant findVariantById(@Param("variantId") Integer variantId);

    void insertVariant(ProductVariant variant);

    void updateVariant(ProductVariant variant);
}
