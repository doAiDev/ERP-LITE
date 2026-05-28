package com.erp.accessory.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper 설정
 * - Entity <-> DTO 변환에 사용
 */
@Configuration
public class ModelMapperConfig {

    /**
     * ModelMapper 빈 등록
     * - STRICT 매칭 전략: 필드명이 정확히 일치해야 매핑
     * - 필드 직접 접근 활성화 (getter/setter 없어도 매핑 가능)
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            // 엄격한 매칭 전략 (오매핑 방지)
            .setMatchingStrategy(MatchingStrategies.STRICT)
            // private 필드 접근 허용
            .setFieldAccessLevel(
                org.modelmapper.config.Configuration.AccessLevel.PRIVATE
            )
            .setFieldMatchingEnabled(true);
        return mapper;
    }
}
