package com.nhnacademy.orderpaymentrefund.dto.order.request.client;

import com.nhnacademy.orderpaymentrefund.dto.order.request.field.OrderItemDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public record ClientViewOrderPostRequestDto ( // 회원이 주문 시도했을 때 필요한 dtㅐ
     List<OrderItemDto> orderItemDtoList // 주문 목록
)
{

}
