package com.nhnacademy.orderpaymentrefund.dto.order.request;

import java.util.List;

public interface OrderForm {
    List<OrderDetailDtoItem> getOrderItemList();
    int getOrderDetailSize();
    Long getTotalPayAmount();
}
