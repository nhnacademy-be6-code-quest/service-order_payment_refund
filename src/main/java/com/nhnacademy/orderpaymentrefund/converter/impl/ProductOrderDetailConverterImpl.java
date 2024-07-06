package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderForm;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailConverterImpl implements ProductOrderDetailConverter {

    public ProductOrderDetail dtoToEntity(ClientOrderForm.OrderDetailDtoItem dto, Order order){
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .pricePerProduct(dto.getProductSinglePrice())
                .build();
    }

    public ProductOrderDetail dtoToEntity(ProductOrderDetailDto dto, Order order) {
        return ProductOrderDetail.builder()
                .order(order)
                .productId(dto.productId())
                .quantity(dto.quantity())
                .pricePerProduct(dto.productSinglePrice())
                .build();
    }

    public ProductOrderDetailDto entityToDto(ProductOrderDetail entity) {
        return ProductOrderDetailDto.builder()
                .productId(entity.getProductId())
                .productSinglePrice(entity.getPricePerProduct())
                .quantity(entity.getQuantity())
                .build();
    }

}
