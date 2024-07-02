package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Builder;
import lombok.Getter;

@Builder
public record FindNonClientOrderPasswordRequestDto(
    long orderId,
    String ordererName,
    String phoneNumber,
    String email
){
}
