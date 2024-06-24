package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.Builder;

@Builder
public record ProductItemDto (
    long id,
    String imgPath,
    String name,
    long price,
    long quantity
)
{

}
