package com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import lombok.Builder;

@Builder
public record AdminShippingPolicyPutRequestDto (
    String description,
    int shippingFee,
    int minPurchaseAmount,
    ShippingPolicyType shippingPolicyType
)
{
}
