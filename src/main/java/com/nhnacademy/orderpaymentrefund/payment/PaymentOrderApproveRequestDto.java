package com.nhnacademy.orderpaymentrefund.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderApproveRequestDto {

    long orderTotalAmount;
    long discountAmountByCoupon;
    String tossOrderId;

    Long clientId;
    Long couponId;
    long discountAmountByPoint;
    long accumulatedPoint;
    List<ProductOrderDetailRequestDto> productOrderDetailList;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductOrderDetailRequestDto {
        long productId;
        long quantity;
        List<ProductOrderDetailOptionRequestDto> productOrderDetailOptionRequestDtoList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductOrderDetailOptionRequestDto {
        long productId;
        long optionProductQuantity;
    }

}