package com.nhnacademy.orderpaymentrefund.dto.order.request;

import java.time.LocalDate;
import java.util.List;

public interface OrderForm {
    List<OrderDetailDtoItem> getOrderItemList();
    int getOrderDetailSize();
    Long getTotalPayAmount();
    String getOrderCode();
    Long getProductTotalAmount();
    Integer getShippingFee();
    LocalDate getDesignatedDeliveryDate();
    String getPhoneNumber();
    String getDeliveryAddress();
}
