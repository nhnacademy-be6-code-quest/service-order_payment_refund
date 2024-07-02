package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;

public interface CommonOrderService {
    void createOrderProductDetailAndOption(Order order, OrderedProductAndOptionProductPairDto productAndOptionProductPair);
}
