package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailOptionDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductOrderDetailOptionConverter implements com.nhnacademy.orderpaymentrefund.converter.ProductOrderDetailOptionConverter {

    public ProductOrderDetailOption dtoToEntity(ClientOrderFormRequestDto.OrderDetailDtoItem dto, ProductOrderDetail productOrderDetail){
        return ProductOrderDetailOption.builder()
                .productId(dto.getProductId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(dto.getOptionProductName())
                .optionProductPrice(Optional.ofNullable(dto.getOptionProductSinglePrice()).orElse(0L))
                .build();
    }

    public ProductOrderDetailOption dtoToEntity(ProductOrderDetailOptionDto dto, ProductOrderDetail productOrderDetail) {
        return ProductOrderDetailOption.builder()
                .productId(dto.productId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(dto.optionProductName())
                .optionProductPrice(dto.optionProductSinglePrice())
                .build();
    }

    public ProductOrderDetailOptionDto entityToDto(ProductOrderDetailOption entity){
        return ProductOrderDetailOptionDto.builder()
                .productId(entity.getProductId())
                .optionProductName(entity.getOptionProductName())
                .optionProductSinglePrice(entity.getOptionProductPrice())
                .quantity(entity.getQuantity())
                .build();
    }

}
