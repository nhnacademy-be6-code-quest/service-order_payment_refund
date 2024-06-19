package com.nhnacademy.order.dto.order.request.client;

import com.nhnacademy.order.dto.order.request.field.OrderItem;

import java.util.List;

public class ClientOrderPostRequestDto { // 회원이 주문 시도했을 때 필요한 dto
    private List<OrderItem> orderItemList;
}
