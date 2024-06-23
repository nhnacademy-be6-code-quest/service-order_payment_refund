package com.nhnacademy.orderpaymentrefund.dto.shipping.admin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminShippingPolicyPutRequestDto {
    private String description;
    private int fee;
    private int lowerBound;
}
