package com.erp.accessory.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc OpenAPI (Swagger) 설정
 * - JWT Bearer 인증 스키마 등록
 * - API 기본 정보 설정
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * OpenAPI 기본 설정
     * - JWT Bearer 토큰 인증 방식 등록
     * - Swagger UI에서 우측 상단 자물쇠 아이콘으로 토큰 입력 가능
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(apiInfo())
            // 전역 보안 요구사항 적용
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            // 보안 스키마 컴포넌트 등록
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme())
            );
    }

    /**
     * API 기본 정보
     */
    private Info apiInfo() {
        return new Info()
            .title("다점포 악세사리 ERP 시스템 API")
            .description(
                "악세사리 가게 다점포 관리를 위한 ERP 백엔드 API\n\n" +
                "## 주요 기능\n" +
                "- 점포 및 직원 관리\n" +
                "- 상품/재고 관리 (다점포 재고 이동)\n" +
                "- 판매 관리 및 일일 정산\n" +
                "- 고객 관리 (포인트/등급)\n" +
                "- 발주/입고 관리"
            )
            .version("1.0.0")
            .contact(new Contact()
                .name("ERP 관리자")
                .email("admin@accessory-erp.com")
            );
    }

    /**
     * JWT Bearer 보안 스키마 설정
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");
    }
}
