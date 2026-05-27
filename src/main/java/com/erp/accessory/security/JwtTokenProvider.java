package com.erp.accessory.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증 컴포넌트
 * - jjwt 0.12.5 API 사용
 * - Access Token + Refresh Token 지원
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-token-expiry}")
    private long accessTokenExpiry;   // 밀리초

    @Value("${app.jwt.refresh-token-expiry}")
    private long refreshTokenExpiry;  // 밀리초

    /**
     * 서명 키 생성 (HMAC-SHA256)
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token 생성
     * @param userId   auth_users.user_id
     * @param username 로그인 아이디
     * @param role     사용자 권한
     * @param staffId  직원 ID
     * @param storeId  소속 점포 ID
     */
    public String generateAccessToken(Integer userId, String username,
                                       String role, Integer staffId, Integer storeId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpiry);

        return Jwts.builder()
            .subject(String.valueOf(userId))       // sub: userId
            .claim("username", username)
            .claim("role", role)
            .claim("staffId", staffId)
            .claim("storeId", storeId)
            .claim("type", "access")
            .issuedAt(now)
            .expiration(expiry)
            .signWith(getSigningKey())              // jjwt 0.12.5: signWith(key)
            .compact();
    }

    /**
     * Refresh Token 생성
     * @param userId auth_users.user_id
     */
    public String generateRefreshToken(Integer userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpiry);

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiry)
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * 토큰에서 Claims 추출
     * - jjwt 0.12.5 API: Jwts.parser().verifyWith(key).build()
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())            // 서명 검증 키 설정
            .build()
            .parseSignedClaims(token)              // 서명된 클레임 파싱
            .getPayload();
    }

    /**
     * 토큰에서 userId(subject) 추출
     */
    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getClaims(token).getSubject());
    }

    /**
     * 토큰에서 username 추출
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).get("username", String.class);
    }

    /**
     * 토큰에서 role 추출
     */
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    /**
     * 토큰 유효성 검증
     * @return true: 유효, false: 유효하지 않음
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token); // 예외 없으면 유효
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT 토큰 만료: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT 토큰: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 토큰 형식: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 비어있음: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 토큰 타입 확인 (access/refresh)
     */
    public String getTokenType(String token) {
        return getClaims(token).get("type", String.class);
    }
}
