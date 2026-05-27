package com.erp.accessory.security;

import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security 사용자 로드 서비스
 * - loadUserByUsername: 로그인 시 username으로 사용자 조회
 * - loadUserByUserId: JWT 필터에서 userId로 사용자 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StoreMapper storeMapper;

    /**
     * username으로 사용자 로드 (Spring Security 기본 메서드)
     * - 로그인 인증 시 사용
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = storeMapper.findAuthUserByUsername(username);
        if (authUser == null) {
            log.warn("사용자를 찾을 수 없음 - username: {}", username);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        if (!authUser.isActive()) {
            log.warn("비활성화된 계정 로그인 시도 - username: {}", username);
            throw new UsernameNotFoundException("비활성화된 계정입니다: " + username);
        }
        return new UserPrincipal(authUser);
    }

    /**
     * userId로 사용자 로드 (JWT 필터에서 사용)
     * - JWT 토큰 검증 후 SecurityContext 설정 시 사용
     * @param userId auth_users.user_id
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    public UserPrincipal loadUserByUserId(Integer userId) {
        AuthUser authUser = storeMapper.findAuthUserById(userId);
        if (authUser == null) {
            log.warn("사용자를 찾을 수 없음 - userId: {}", userId);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: userId=" + userId);
        }
        return new UserPrincipal(authUser);
    }
}
