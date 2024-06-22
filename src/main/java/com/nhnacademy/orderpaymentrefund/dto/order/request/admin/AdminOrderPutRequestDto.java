package com.nhnacademy.orderpaymentrefund.dto.order.request.admin;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;

public class AdminOrderPutRequestDto { // 관리자가 주문에 대해 수정할 사항들.
    private OrderStatus orderStatus;
}