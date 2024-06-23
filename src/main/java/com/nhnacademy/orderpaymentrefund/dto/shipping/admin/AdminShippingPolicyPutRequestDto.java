package com.nhnacademy.orderpaymentrefund.dto.shipping.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record AdminShippingPolicyPutRequestDto (
    String description,
    int fee,
    int lowerBound
)
{

}
