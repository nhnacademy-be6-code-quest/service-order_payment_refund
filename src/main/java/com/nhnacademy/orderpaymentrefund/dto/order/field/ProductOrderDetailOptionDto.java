package com.nhnacademy.orderpaymentrefund.dto.order.field;

import lombok.Builder;

@Builder
public record ProductOrderDetailOptionDto(
        long productId,
        String optionProductName,
        long optionProductSinglePrice,
        long quantity
){
}