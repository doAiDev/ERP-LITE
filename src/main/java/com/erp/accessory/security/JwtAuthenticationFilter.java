package com.erp.accessory.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * - 모든 요청에 대해 한 번씩 실행 (OncePerRequestFilter)
 * - Authorization 헤더에서 Bearer 토큰 추출 후 검증
 * - 유효한 토큰이면 SecurityContext에 인증 정보 설정
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. Authorization 헤더에서 Bearer 토큰 추출
            String token = extractTokenFromRequest(request);

            // 2. 토큰이 존재하고 유효한 경우 인증 처리
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                // access 토큰인지 확인 (refresh 토큰은 인증에 사용 불가)
                String tokenType = jwtTokenProvider.getTokenType(token);
                if (!"access".equals(tokenType)) {
                    log.warn("Refresh 토큰으로 API 접근 시도: {}", request.getRequestURI());
                    filterChain.doFilter(request, response);
                    return;
                }

                // 3. userId로 사용자 정보 로드
                Integer userId = jwtTokenProvider.getUserIdFromToken(token);
                UserPrincipal userPrincipal =
                    customUserDetailsService.loadUserByUserId(userId);

                // 4. SecurityContext에 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                    );
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT 인증 성공 - userId: {}, username: {}, URI: {}",
                    userId, userPrincipal.getUsername(), request.getRequestURI());
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 Bearer 토큰 추출
     * Authorization: Bearer <token>
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 토큰 부분만 추출
        }
        return null;
    }
}
