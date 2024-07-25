package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
<<<<<<<< HEAD:src/main/java/com/nhnacademy/orderpaymentrefund/dto/payment/request/ApprovePaymentRequestDto.java
public class ApprovePaymentRequestDto {
    String orderCode; // 토스 오더 아이디
    long amount;
    String paymentKey;
    String methodType;
========
public class TossPaymentRequestDto {
    String orderId; // 토스 오더 아이디
    long amount;
    String paymentKey;

>>>>>>>> 3a7ee66 (feat: 추가):src/main/java/com/nhnacademy/orderpaymentrefund/dto/payment/request/TossPaymentRequestDto.java
}
