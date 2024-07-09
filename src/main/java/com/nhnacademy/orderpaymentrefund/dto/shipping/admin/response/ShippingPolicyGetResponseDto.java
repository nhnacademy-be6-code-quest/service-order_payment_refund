package com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import lombok.Builder;
import lombok.Getter;

@Builder
public record ShippingPolicyGetResponseDto (
    String description,
    int shippingFee,
    int minPurchaseAmount,
    ShippingPolicyType shippingPolicyType
){
}