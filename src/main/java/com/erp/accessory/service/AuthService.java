package com.erp.accessory.service;

import com.erp.accessory.dto.request.LoginRequest;
import com.erp.accessory.dto.request.RegisterRequest;
import com.erp.accessory.dto.response.TokenResponse;
import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.entity.Staff;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.StoreMapper;
import com.erp.accessory.security.JwtTokenProvider;
import com.erp.accessory.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final StoreMapper storeMapper;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest request) {
        Authentication auth;
        try {
            auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (DisabledException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "계정 승인 대기 중입니다. 관리자에게 문의하세요.");
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
        AuthUser authUser = storeMapper.findAuthUserById(userId);
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

    @Transactional
    public void register(RegisterRequest request) {
        if (storeMapper.findAuthUserByUsername(request.getUsername()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_ENTRY, "이미 사용 중인 아이디입니다.");
        }
        Staff staff = new Staff();
        staff.setStoreId(request.getStoreId());
        staff.setName(request.getName());
        staff.setPhone(request.getPhone());
        staff.setRole("staff");
        storeMapper.insertStaff(staff);

        AuthUser authUser = new AuthUser();
        authUser.setStaffId(staff.getStaffId());
        authUser.setUsername(request.getUsername());
        authUser.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        storeMapper.insertAuthUserPending(authUser);
    }
}
