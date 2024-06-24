package com.nhnacademy.orderpaymentrefund.dto.order.request.client;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
public record ClientOrderPutRequestDto (
    OrderStatus orderStatus
)
{

}
