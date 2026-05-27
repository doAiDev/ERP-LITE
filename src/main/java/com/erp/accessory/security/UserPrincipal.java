package com.erp.accessory.security;

import com.erp.accessory.entity.AuthUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security UserDetails 구현체
 * - JWT 토큰에서 추출한 사용자 정보를 담는 Principal 객체
 * - userId, staffId, storeId, role 정보 포함
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Integer userId;    // auth_users.user_id
    private final Integer staffId;   // staff.staff_id
    private final Integer storeId;   // staff.store_id (소속 점포)
    private final String username;   // 로그인 아이디
    private final String password;   // BCrypt 해시
    private final String role;       // ADMIN / MANAGER / STAFF
    private final boolean active;    // 계정 활성화 여부

    /**
     * AuthUser 엔티티에서 UserPrincipal 생성
     */
    public UserPrincipal(AuthUser authUser) {
        this.userId   = authUser.getUserId();
        this.staffId  = authUser.getStaffId();
        this.storeId  = authUser.getStoreId();
        this.username = authUser.getUsername();
        this.password = authUser.getPasswordHash();
        this.role     = authUser.getRole();
        this.active   = authUser.isActive();
    }

    /**
     * 권한 목록 반환
     * - Spring Security는 ROLE_ 접두사 필요 → ROLE_ADMIN, ROLE_MANAGER, ROLE_STAFF
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /** 계정 만료 여부 (항상 미만료) */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** 계정 잠금 여부 (항상 미잠금) */
    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    /** 자격 증명 만료 여부 (항상 미만료) */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 계정 활성화 여부 */
    @Override
    public boolean isEnabled() {
        return active;
    }
}
