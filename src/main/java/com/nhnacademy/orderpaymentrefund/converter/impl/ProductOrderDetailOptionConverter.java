package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderDetailOptionConverter {

    public ProductOrderDetailOption dtoToEntity(ClientOrderCreateForm.OrderDetailDtoItem dto, ProductOrderDetail productOrderDetail){
        return ProductOrderDetailOption.builder()
                .productId(dto.getProductId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(dto.getOptionProductName())
                .optionProductPrice(dto.getOptionProductSinglePrice() == null ? 0 : dto.getOptionProductSinglePrice())
                .build();
    }

    public ProductOrderDetailOption dtoToEntity(NonClientOrderForm.OrderDetailDtoItem dto, ProductOrderDetail productOrderDetail){
        return ProductOrderDetailOption.builder()
                .productId(dto.getOptionProductId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(dto.getOptionProductName())
                .optionProductPrice(dto.getOptionProductSinglePrice() == null ? 0 : dto.getOptionProductSinglePrice())
                .build();
    }

}
