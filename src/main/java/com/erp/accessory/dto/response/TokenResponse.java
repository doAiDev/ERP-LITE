package com.erp.accessory.dto.response;

import lombok.Builder;
import lombok.Data;

/** JWT 토큰 응답 DTO */
@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
    private Integer storeId;
    private Integer staffId;
}
