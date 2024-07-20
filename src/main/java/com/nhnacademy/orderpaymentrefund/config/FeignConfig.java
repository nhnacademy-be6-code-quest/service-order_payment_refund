package com.nhnacademy.orderpaymentrefund.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            log.info("Feign Request: {} {}", template.method(), template.url());
            log.info("Headers: {}", template.headers());
        };
    }
}
