package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProductOrderDetailOptionResponseDto {
    private Long productId;
    private Long productOrderDetailId;
    private String optionProductName;
    private Long optionProductPrice;
    private Long optionProductQuantity;
}
