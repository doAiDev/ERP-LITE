package com.erp.accessory.security;

import com.erp.accessory.entity.AuthUser;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security 인증 주체 - 로그인한 사용자 정보
 */
@Getter
@Builder
public class UserPrincipal implements UserDetails {

    private final Integer userId;
    private final Integer staffId;
    private final Integer storeId;
    private final String username;
    private final String password;
    private final String role;
    private final boolean active;

    /** AuthUser 엔티티로부터 생성 */
    public static UserPrincipal fromAuthUser(AuthUser authUser) {
        return UserPrincipal.builder()
                .userId(authUser.getUserId())
                .staffId(authUser.getStaffId())
                .storeId(authUser.getStoreId())
                .username(authUser.getUsername())
                .password(authUser.getHashedPassword())
                .role(authUser.getRole())
                .active(authUser.isActive())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return active; }
}
