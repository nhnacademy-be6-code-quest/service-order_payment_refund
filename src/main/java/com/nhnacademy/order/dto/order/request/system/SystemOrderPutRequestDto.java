package com.nhnacademy.order.dto.order.request.system;

import com.nhnacademy.order.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SystemOrderPutRequestDto {
    private OrderStatus orderStatus;
}
