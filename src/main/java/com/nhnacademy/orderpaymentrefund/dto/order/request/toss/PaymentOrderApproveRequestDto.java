package com.nhnacademy.orderpaymentrefund.dto.order.request.toss;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderApproveRequestDto {

    long orderTotalAmount;
    long discountAmountByCoupon;
    String orderCode;

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