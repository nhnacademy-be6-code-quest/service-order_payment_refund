package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.Converter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class NonClientOrderConverter implements Converter<Order, CreateNonClientOrderRequestDto> {

    @Override
    public Order dtoToEntity(CreateNonClientOrderRequestDto dto) {

        StringBuilder address = new StringBuilder();
        address.append(dto.nonClientOrdererInfoDto().zipCode());
        address.append(", ");
        address.append(dto.nonClientOrdererInfoDto().address());
        address.append(", ");
        address.append(dto.nonClientOrdererInfoDto().detailAddress());

        return Order.nonClientOrderBuilder()
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(dto.nonClientOrderPriceInfoDto().productTotalAmount())
                .shippingFee(dto.nonClientOrderPriceInfoDto().shippingFee())
                .designatedDeliveryDate(dto.designatedDeliveryDate())
                .phoneNumber(dto.nonClientOrdererInfoDto().phoneNumber())
                .deliveryAddress(address.toString())
                .nonClientOrderPassword(dto.nonClientOrdererInfoDto().nonClientOrderPassword())
                .nonClientOrdererName(dto.nonClientOrdererInfoDto().nonClientOrdererName())
                .nonClientOrdererEmail(dto.nonClientOrdererInfoDto().nonClientOrdererEmail())
                .build();

    }

}
