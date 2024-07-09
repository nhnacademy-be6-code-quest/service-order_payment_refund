package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Builder;
import lombok.Getter;

@Builder
public record FindNonClientOrderIdRequestDto (
    String ordererName,
    String phoneNumber,
    String email
){
}
