package com.nhnacademy.orderpaymentrefund.dto.order.request.field;

import lombok.Builder;
import lombok.Getter;

@Builder
public record OrderDetailDto (
    long productId,
    long price,
    long quantity
)
{

}