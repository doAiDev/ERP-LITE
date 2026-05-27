package com.erp.accessory.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantRequest {

    @NotBlank(message = "SKU를 입력해 주세요")
    @Size(max = 50)
    private String sku;

    @Size(max = 50)
    private String color;

    @Size(max = 50)
    private String size;

    @Size(max = 50)
    private String material;

    private Boolean active;
}
