package com.example.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LoggingConfig {

    @Bean
    public CommandLineRunner logStartup() {
        return args -> {
            log.debug("애플리케이션이 디버그 레벨로 시작되었습니다.");
            log.info("애플리케이션이 성공적으로 시작되었습니다.");
            log.warn("이것은 경고 로그입니다.");
            log.error("이것은 에러 로그입니다. (테스트용)");
        };
    }
} 