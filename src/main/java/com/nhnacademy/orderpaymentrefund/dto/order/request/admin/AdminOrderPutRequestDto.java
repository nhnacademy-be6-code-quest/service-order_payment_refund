package com.nhnacademy.orderpaymentrefund.dto.order.request.admin;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminOrderPutRequestDto { // 관리자가 주문에 대해 수정할 사항들.
    private OrderStatus orderStatus;
}