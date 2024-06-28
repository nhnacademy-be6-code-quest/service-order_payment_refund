package com.nhnacademy.orderpaymentrefund.dto.shipping.admin;

import lombok.Builder;

@Builder
public record AdminShippingPolicyPutRequestDto (
    String description,
    int fee,
    int lowerBound
)
{

}
