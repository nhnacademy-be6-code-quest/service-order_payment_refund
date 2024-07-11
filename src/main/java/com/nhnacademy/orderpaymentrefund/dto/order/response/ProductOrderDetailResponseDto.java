package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderDetailResponseDto {
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Long pricePerProduct;
    private String productName;
}
