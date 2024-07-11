package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderFormRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailConverterImpl implements ProductOrderDetailConverter {

    public ProductOrderDetail dtoToEntity(ClientOrderFormRequestDto.OrderDetailDtoItem dto, Order order){
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .pricePerProduct(dto.getProductSinglePrice())
                .productName(dto.getProductName())
                .build();
    }

    public ProductOrderDetail dtoToEntity(NonClientOrderFormRequestDto.OrderDetailDtoItem dto, Order order){
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .pricePerProduct(dto.getProductSinglePrice())
                .productName(dto.getProductName())
                .build();
    }

}
