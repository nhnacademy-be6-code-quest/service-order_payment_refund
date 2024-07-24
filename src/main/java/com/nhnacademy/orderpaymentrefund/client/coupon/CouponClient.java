package com.nhnacademy.orderpaymentrefund.client.coupon;

import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "couponClient", url = "http://localhost:8001")
public interface CouponClient {

    @GetMapping("/api/coupon")
    List<CouponOrderResponseDto> findClientCoupon(@RequestHeader HttpHeaders headers);

}
