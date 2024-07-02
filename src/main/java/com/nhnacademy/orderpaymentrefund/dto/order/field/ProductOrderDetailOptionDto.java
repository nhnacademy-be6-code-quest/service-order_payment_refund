package com.nhnacademy.orderpaymentrefund.dto.order.field;

import lombok.Builder;

@Builder
public record ProductOrderDetailOptionDto(
        long optionProductId,
        String optionProductName,
        long optionProductSinglePrice,
        long quantity
){
}