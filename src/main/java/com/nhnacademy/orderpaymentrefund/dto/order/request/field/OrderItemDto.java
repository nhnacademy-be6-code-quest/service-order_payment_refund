package com.nhnacademy.orderpaymentrefund.dto.order.request.field;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemDto {
    private long productId;
    private long quantity;
}