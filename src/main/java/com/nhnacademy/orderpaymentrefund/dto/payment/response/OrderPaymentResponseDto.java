package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderPaymentResponseDto {
    long totalPrice;
    @NotNull
    List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList;
}