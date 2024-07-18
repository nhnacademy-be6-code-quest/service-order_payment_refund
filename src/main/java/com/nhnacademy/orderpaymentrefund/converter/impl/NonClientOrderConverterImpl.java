package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.NonClientOrderConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NonClientOrderConverterImpl implements NonClientOrderConverter {

    public Order dtoToEntity(NonClientOrderForm requestDto) {

        StringBuilder address = new StringBuilder();
        address.append(requestDto.getAddressZipCode());
        address.append(", ");
        address.append(requestDto.getDeliveryAddress());

        return Order.nonClientOrderBuilder()
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(requestDto.getProductTotalAmount())
                .shippingFee(requestDto.getShippingFee())
                .designatedDeliveryDate(requestDto.getDesignatedDeliveryDate())
                .phoneNumber(requestDto.getPhoneNumber())
                .deliveryAddress(address.toString())
                .nonClientOrderPassword(requestDto.getOrderPassword())
                .nonClientOrdererName(requestDto.getOrderedPersonName())
                .nonClientOrdererEmail(requestDto.getEmail())
                .build();

    }


}
