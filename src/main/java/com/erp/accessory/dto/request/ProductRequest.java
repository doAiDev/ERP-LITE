package com.erp.accessory.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "상품명을 입력해 주세요")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "카테고리를 입력해 주세요")
    private String category;

    @Size(max = 50)
    private String brand;

    @NotNull(message = "원가를 입력해 주세요")
    @DecimalMin(value = "0")
    private BigDecimal costPrice;

    @NotNull(message = "판매가를 입력해 주세요")
    @DecimalMin(value = "0")
    private BigDecimal sellingPrice;

    private Boolean active;

    @Valid
    private List<VariantRequest> variants;

    @Getter
    @Setter
    public static class VariantRequest {

        @NotBlank(message = "SKU를 입력해 주세요")
        @Size(max = 50)
        private String sku;

        @Size(max = 50)
        private String color;

        @Size(max = 50)
        private String size;

        @Size(max = 50)
        private String material;
    }
}
