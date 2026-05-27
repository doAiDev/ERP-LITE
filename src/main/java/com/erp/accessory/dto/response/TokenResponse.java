package com.erp.accessory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JWT 토큰 응답 DTO
 * - 로그인 성공 시 되돌려주는 토큰 정보
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;   // Access JWT 토큰
    private String refreshToken;  // Refresh JWT 토큰
    private String tokenType;     // 토큰 타입 (Bearer)
    private String role;          // 사용자 권한
    private Integer storeId;      // 소속 점포 ID
    private Integer staffId;      // 직원 ID
    private String username;      // 사용자명
    private Long expiresIn;       // 액세스 토큰 만료 시간 (초)
}
