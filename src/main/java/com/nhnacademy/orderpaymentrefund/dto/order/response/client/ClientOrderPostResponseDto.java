package com.nhnacademy.orderpaymentrefund.dto.order.response.client;

import lombok.Builder;

@Builder
public record ClientOrderPostResponseDto(
        long orderId,
        long totalProductPrice
)
{
}
