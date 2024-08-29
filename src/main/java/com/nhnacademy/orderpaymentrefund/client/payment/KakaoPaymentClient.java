package com.nhnacademy.orderpaymentrefund.client.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.KakaoPaymentReadyRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.KakaoPaymentReadyResponseDto;
import com.nhnacademy.orderpaymentrefund.interceptor.FeignClientRequestLogger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoClientPayment", url = "https://open-api.kakaopay.com", configuration = FeignClientRequestLogger.class)
public interface KakaoPaymentClient {
    @PostMapping(value = "/online/v1/payment/ready", headers = {"Content-Type:application/json"})
    KakaoPaymentReadyResponseDto readyKakaoPayment(
            @RequestHeader("Host") String host,
            @RequestHeader("Authorization") String authorization,
            @RequestBody KakaoPaymentReadyRequestDto requestDto
    );

}
