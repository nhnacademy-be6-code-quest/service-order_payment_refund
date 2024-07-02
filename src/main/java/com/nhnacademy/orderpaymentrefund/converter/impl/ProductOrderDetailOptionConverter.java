package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.Converter;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailOptionDto;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailOptionConverter implements Converter<ProductOrderDetailOption, ProductOrderDetailOptionDto> {

    @Override
    public ProductOrderDetailOption dtoToEntity(ProductOrderDetailOptionDto dto, Object... objects) {
        if(objects.length == 1 && objects[0] instanceof ProductOrderDetail) {
            return ProductOrderDetailOption.builder()
                    .productId(dto.optionProductId())
                    .productOrderDetail((ProductOrderDetail) objects[0])
                    .optionProductName(dto.optionProductName())
                    .optionProductPrice(dto.optionProductSinglePrice())
                    .build();
        }
        else {
            throw new IllegalArgumentException("dto 인자를 제외한 인자는 ProductOrderDetail 타입의 인자만 올 수 있습니다.");
        }
    }

}
