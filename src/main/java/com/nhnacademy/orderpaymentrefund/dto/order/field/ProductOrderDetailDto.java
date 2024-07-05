package com.nhnacademy.orderpaymentrefund.dto.order.field;

import lombok.Builder;

@Builder
public record ProductOrderDetailDto (
        long productId,
        long productSinglePrice,
        long quantity
){
}
