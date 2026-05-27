package com.erp.accessory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring MVC 설정
 * - CORS 설정: 프론트엔드(localhost:3000) 허용
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    /**
     * CORS 설정
     * - 허용 Origin: application.yml의 app.cors.allowed-origins
     * - 허용 메서드: GET, POST, PUT, PATCH, DELETE, OPTIONS
     * - 인증 헤더 허용: Authorization, Content-Type
     * - 최대 캐시 시간: 3600초
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins.toArray(new String[0]))
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
            )
            .exposedHeaders("Authorization")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
