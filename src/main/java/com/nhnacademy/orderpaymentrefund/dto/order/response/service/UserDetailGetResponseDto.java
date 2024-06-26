package com.nhnacademy.orderpaymentrefund.dto.order.response.service;

import lombok.Builder;

@Builder
public record UserDetailGetResponseDto(
        long productId,
        long quantity,
        long priceProduct
){}
