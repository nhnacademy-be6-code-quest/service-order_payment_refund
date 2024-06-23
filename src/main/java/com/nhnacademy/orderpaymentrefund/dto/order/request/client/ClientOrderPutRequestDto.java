package com.nhnacademy.orderpaymentrefund.dto.order.request.client;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientOrderPutRequestDto {
    private OrderStatus orderStatus;
}
