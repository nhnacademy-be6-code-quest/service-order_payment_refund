package com.nhnacademy.orderpaymentrefund.dto.order.request.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetailDto {
    private long productId;
    private long price;
    private long quantity;
}