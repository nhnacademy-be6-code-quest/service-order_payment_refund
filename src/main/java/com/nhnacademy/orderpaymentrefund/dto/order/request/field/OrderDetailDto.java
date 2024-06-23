package com.nhnacademy.orderpaymentrefund.dto.order.request.field;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailDto {
    private long productId;
    private long price;
    private long quantity;
}