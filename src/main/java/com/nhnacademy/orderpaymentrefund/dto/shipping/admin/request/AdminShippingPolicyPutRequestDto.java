package com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record AdminShippingPolicyPutRequestDto (
    String description,
    int shippingFee,
    int minPurchaseAmount,
    ShippingPolicyType shippingPolicyType
)
{
}
