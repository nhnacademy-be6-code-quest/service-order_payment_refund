package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailConverter {

    public ProductOrderDetail dtoToEntity(OrderDetailDtoItem dto, Order order){
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .pricePerProduct(dto.getProductSinglePrice())
                .productName(dto.getProductName())
                .build();
    }

}
