package com.erp.accessory.security;

import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * DB에서 사용자 정보를 로드하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StoreMapper storeMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = storeMapper.findAuthUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        return UserPrincipal.fromAuthUser(user);
    }

    /** userId로 사용자 로드 (JWT 필터에서 호출) */
    public UserDetails loadUserByUserId(Integer userId) {
        AuthUser user = storeMapper.findAuthUserById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("사용자 ID를 찾을 수 없습니다: " + userId);
        }
        return UserPrincipal.fromAuthUser(user);
    }
}
