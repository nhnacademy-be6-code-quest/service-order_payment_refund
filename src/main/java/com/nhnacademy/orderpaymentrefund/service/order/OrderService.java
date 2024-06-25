package com.nhnacademy.orderpaymentrefund.service.order;


import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import java.util.Optional;


public interface OrderService {

    Optional<OrderStatus> getOrderStatusByOrderDetailId(long orderDetailId);
}
