package com.nhnacademy.orderpaymentrefund.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignClientRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 로그 요청 URL
        log.info("Request URL: {}", requestTemplate.url());

        // 로그 요청 헤더
        requestTemplate.headers().forEach((key, value) -> log.info("Request Header: {} = {}", key, value));

        // 로그 요청 본문 (선택 사항, 큰 데이터는 피하는 것이 좋습니다)
        if (requestTemplate.body() != null) {
            log.info("Request Body: {}", new String(requestTemplate.body()));
        }
    }
}
