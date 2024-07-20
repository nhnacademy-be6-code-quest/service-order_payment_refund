package com.nhnacademy.orderpaymentrefund.client.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.TossPaymentRefundResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossRefundClient", url = "https://api.tosspayments.com/v1/payments")
public interface TossPayRefundClient {

    @PostMapping("/{paymentKey}/cancel")
    TossPaymentRefundResponseDto cancelPayment(@PathVariable String paymentKey, @RequestBody TossRefundRequestDto tossRefundRequestDto, @RequestHeader(defaultValue = "Authorization") String authorization);
}
