package com.nhnacademy.orderpaymentrefund.dto.order.request.system;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SystemOrderPutRequestDto {
    private OrderStatus orderStatus;
}
