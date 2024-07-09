package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;

public interface AdminOrderService {
    void changeOrderStatus(long orderId, OrderStatus orderStatus);
}
