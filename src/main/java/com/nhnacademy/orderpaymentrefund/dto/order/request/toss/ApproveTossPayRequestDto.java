package com.nhnacademy.orderpaymentrefund.dto.order.request.toss;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApproveTossPayRequestDto {
    String orderId; // uuid
    String paymentKey;
    long amount;

    @Builder
    public ApproveTossPayRequestDto(String orderId, String paymentKey, long amount){
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
