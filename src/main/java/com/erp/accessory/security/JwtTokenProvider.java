package com.erp.accessory.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰 생성·검증 유틸리티
 * - Access Token: 30분 유효
 * - Refresh Token: 7일 유효
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiry}") long accessTokenExpiry,
            @Value("${jwt.refresh-token-expiry}") long refreshTokenExpiry) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public String generateAccessToken(Authentication auth) {
        return buildToken((UserPrincipal) auth.getPrincipal(), accessTokenExpiry);
    }

    public String generateRefreshToken(Authentication auth) {
        return buildToken((UserPrincipal) auth.getPrincipal(), refreshTokenExpiry);
    }

    /** 토큰 직접 생성 (Refresh Token 갱신 시 사용) */
    public String generateTokenFromPrincipal(UserPrincipal principal) {
        return buildToken(principal, accessTokenExpiry);
    }

    private String buildToken(UserPrincipal p, long expiryMs) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(p.getUserId()))
                .claim("role", p.getRole())
                .claim("storeId", p.getStoreId())
                .claim("staffId", p.getStaffId())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiryMs))
                .signWith(key)
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getClaims(token).getSubject());
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 JWT: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
