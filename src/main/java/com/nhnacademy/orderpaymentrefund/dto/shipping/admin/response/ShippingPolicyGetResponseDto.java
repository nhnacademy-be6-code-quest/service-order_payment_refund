package com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response;

import com.nhnacademy.orderpaymentrefundx.domain.shipping.ShippingPolicyType;
import lombok.Builder;

@Builder
public record ShippingPolicyGetResponseDto (
    String description,
    int shippingFee,
    int minPurchaseAmount,
    ShippingPolicyType shippingPolicyType
){
}