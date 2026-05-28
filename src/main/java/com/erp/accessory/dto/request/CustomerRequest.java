package com.erp.accessory.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter @Setter
public class CustomerRequest {
    @NotBlank @Size(max = 50) private String name;
    @NotBlank @Size(max = 20) private String phone;
    @Size(max = 100) private String email;
    private LocalDate birthDate;
    private Integer createdStoreId;
}
