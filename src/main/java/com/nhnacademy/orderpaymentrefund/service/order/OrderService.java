package com.nhnacademy.orderpaymentrefund.service.order;


import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;


public interface OrderService {

    OrderStatus getOrderStatusByOrderDetailId(long orderDetailId);
}
