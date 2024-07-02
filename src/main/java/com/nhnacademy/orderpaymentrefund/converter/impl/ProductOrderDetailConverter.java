package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.Converter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailConverter implements Converter<ProductOrderDetail, ProductOrderDetailDto> {

    @Override
    public ProductOrderDetail dtoToEntity(ProductOrderDetailDto dto, Object... objects) {
        if(objects.length == 1 && objects[0] instanceof Order) {
            return ProductOrderDetail.builder()
                    .order((Order) objects[0])
                    .productId(dto.productId())
                    .quantity(dto.quantity())
                    .pricePerProduct(dto.productSinglePrice())
                    .build();
        }
        else{
            throw new IllegalArgumentException("dto 인자를 제외한 인자는 Object 타입의 order만 올 수 있습니다.");
        }
    }

}
