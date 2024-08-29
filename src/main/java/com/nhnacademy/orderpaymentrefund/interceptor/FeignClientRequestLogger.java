package com.nhnacademy.orderpaymentrefund.interceptor;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientRequestLogger {
    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new FeignClientRequestInterceptor();
    }
}
