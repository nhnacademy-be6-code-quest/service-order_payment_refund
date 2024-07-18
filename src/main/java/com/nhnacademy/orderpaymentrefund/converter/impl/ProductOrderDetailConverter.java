package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailConverter {

    public ProductOrderDetail dtoToEntity(ClientOrderCreateForm.OrderDetailDtoItem dto, Order order){
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .pricePerProduct(dto.getProductSinglePrice())
                .productName(dto.getProductName())
                .build();
    }

    public ProductOrderDetail dtoToEntity(NonClientOrderForm.OrderDetailDtoItem dto, Order order){
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .pricePerProduct(dto.getProductSinglePrice())
                .productName(dto.getProductName())
                .build();
    }

}
