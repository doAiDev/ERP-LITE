package com.erp.accessory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 다점포 악세사리 ERP 애플리케이션 메인 클래스
 */
@SpringBootApplication
@EnableScheduling
public class AccessoryErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccessoryErpApplication.class, args);
    }
}
