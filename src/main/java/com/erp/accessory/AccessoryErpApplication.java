package com.erp.accessory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 다점포 악세사리 ERP 시스템 메인 애플리케이션
 * - Spring Boot 3.2.5 기반
 * - @EnableScheduling: 정기 스케줄링 활성화 (등급 업데이트, 일일 정산 등)
 */
@SpringBootApplication
@EnableScheduling
public class AccessoryErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccessoryErpApplication.class, args);
    }
}
