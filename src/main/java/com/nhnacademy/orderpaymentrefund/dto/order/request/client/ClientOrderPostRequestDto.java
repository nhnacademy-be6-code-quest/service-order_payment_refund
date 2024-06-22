package com.nhnacademy.orderpaymentrefund.dto.order.request.client;

import com.nhnacademy.orderpaymentrefund.dto.order.request.field.OrderItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClientOrderPostRequestDto { // 회원이 주문 시도했을 때 필요한 dto
    private List<OrderItemDto> orderItemDtoList; // 주문 목록
}
