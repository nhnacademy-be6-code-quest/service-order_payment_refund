package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProductOrderDetailResponseDto {
    private Long productOrderDetailId;
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Long pricePerProduct;
    private String productName;
}
