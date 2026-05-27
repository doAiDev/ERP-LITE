package com.erp.accessory.service;

import com.erp.accessory.dto.request.LoginRequest;
import com.erp.accessory.dto.response.TokenResponse;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.StoreMapper;
import com.erp.accessory.security.JwtTokenProvider;
import com.erp.accessory.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스 - 로그인, 토큰 갱신
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final StoreMapper storeMapper;

    public TokenResponse login(LoginRequest request) {
        Authentication auth;
        try {
            auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        storeMapper.updateLastLogin(principal.getUserId());

        return TokenResponse.builder()
                .accessToken(tokenProvider.generateAccessToken(auth))
                .refreshToken(tokenProvider.generateRefreshToken(auth))
                .role(principal.getRole())
                .storeId(principal.getStoreId())
                .staffId(principal.getStaffId())
                .build();
    }

    public TokenResponse refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }
        Integer userId = tokenProvider.getUserIdFromToken(refreshToken);
        var authUser = storeMapper.findAuthUserById(userId);
        if (authUser == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        UserPrincipal principal = UserPrincipal.fromAuthUser(authUser);
        String newAccessToken = tokenProvider.generateTokenFromPrincipal(principal);
        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .role(principal.getRole())
                .storeId(principal.getStoreId())
                .staffId(principal.getStaffId())
                .build();
    }
}
