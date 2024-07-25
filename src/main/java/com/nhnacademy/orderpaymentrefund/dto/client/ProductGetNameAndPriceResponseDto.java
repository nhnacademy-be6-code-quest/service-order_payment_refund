package com.nhnacademy.orderpaymentrefund.dto.client;

import lombok.Builder;

@Builder
public record ProductGetNameAndPriceResponseDto(
    Long productId,
    String productName,
    long productPriceSales
) {
}