package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record PackageItemDto (
    long id,
    String name,
    long price
)
{

}
