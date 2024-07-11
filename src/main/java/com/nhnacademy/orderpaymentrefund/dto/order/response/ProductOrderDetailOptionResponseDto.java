package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderDetailOptionResponseDto {
    private Long productId;
    private Long productOrderDetailId;
    private String optionProductName;
    private Long optionProductPrice;
    private Long optionProductQuantity;
}
