package com.erp.accessory;

import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 개발 환경 초기 계정 생성기
 * data.sql 실행 후 auth_users를 BCrypt 인코딩하여 생성
 * 기본 계정: admin/admin1234, manager1/manager1234, staff1/staff1234
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StoreMapper storeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            if (storeMapper.findAuthUserByUsername("admin") == null) {
                createUser(1, "admin",    "admin1234");
                createUser(2, "manager1", "manager1234");
                createUser(3, "staff1",   "staff1234");
                createUser(4, "manager2", "manager1234");
                createUser(6, "manager3", "manager1234");
                log.info("===== 초기 계정 생성 완료 =====");
                log.info("admin    / admin1234    (본사 관리자)");
                log.info("manager1 / manager1234  (강남점 매니저)");
                log.info("staff1   / staff1234    (강남점 직원)");
            }
        } catch (Exception e) {
            log.warn("초기 계정 생성 실패 (data.sql이 실행되지 않았을 수 있음): {}", e.getMessage());
        }
    }

    private void createUser(int staffId, String username, String password) {
        AuthUser user = new AuthUser();
        user.setStaffId(staffId);
        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(password));
        storeMapper.insertAuthUser(user);
    }
}
