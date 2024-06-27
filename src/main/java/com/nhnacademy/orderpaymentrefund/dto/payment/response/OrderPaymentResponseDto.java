package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import java.util.List;
import lombok.Builder;

@Builder
public class OrderPaymentResponseDto {
    long totalPrice;
    List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList;
}