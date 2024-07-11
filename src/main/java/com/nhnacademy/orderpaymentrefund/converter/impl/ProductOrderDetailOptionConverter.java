package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailOptionDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderFormRequestDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductOrderDetailOptionConverter implements com.nhnacademy.orderpaymentrefund.converter.ProductOrderDetailOptionConverter {

    public ProductOrderDetailOption dtoToEntity(ClientOrderFormRequestDto.OrderDetailDtoItem dto, ProductOrderDetail productOrderDetail){
        return ProductOrderDetailOption.builder()
                .productId(dto.getProductId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(dto.getOptionProductName())
                .optionProductPrice(dto.getOptionProductSinglePrice() == null ? 0 : dto.getOptionProductSinglePrice())
                .build();
    }

    public ProductOrderDetailOption dtoToEntity(NonClientOrderFormRequestDto.OrderDetailDtoItem dto, ProductOrderDetail productOrderDetail){
        return ProductOrderDetailOption.builder()
                .productId(dto.getOptionProductId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(dto.getOptionProductName())
                .optionProductPrice(dto.getOptionProductSinglePrice() == null ? 0 : dto.getOptionProductSinglePrice())
                .build();
    }

}
