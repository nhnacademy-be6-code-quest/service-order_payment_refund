package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindNonClientOrderPasswordRequestDto{
    long orderId;
    String ordererName;
    String phoneNumber;
    String email;
}