package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
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

    // TODO 상품주문상세에 '상품_이름' 컬럼 추가함으로써 밑에 있는 두 메서드 수정해야함.

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
