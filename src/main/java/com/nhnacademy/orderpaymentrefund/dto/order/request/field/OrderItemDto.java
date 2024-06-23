package com.nhnacademy.orderpaymentrefund.dto.order.request.field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record OrderItemDto (
    long productId,
    long quantity
)
{

}
