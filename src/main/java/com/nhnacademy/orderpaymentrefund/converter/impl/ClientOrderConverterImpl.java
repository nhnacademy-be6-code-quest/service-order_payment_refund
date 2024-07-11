package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.ClientOrderConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClientOrderConverterImpl implements ClientOrderConverter {

    public Order dtoToEntity(ClientOrderFormRequestDto requestDto, long clientId) {

        StringBuilder address = new StringBuilder();
        address.append(requestDto.getAddressZipCode());
        address.append(", ");
        address.append(requestDto.getDeliveryAddress());
        return Order.clientOrderBuilder()
                .clientId(clientId)
                .couponId(requestDto.getCouponId())
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(requestDto.getProductTotalAmount())
                .shippingFee(requestDto.getShippingFee())
                .designatedDeliveryDate(requestDto.getDesignatedDeliveryDate())
                .phoneNumber(requestDto.getPhoneNumber())
                .deliveryAddress(address.toString())
                .discountAmountByCoupon(Optional.ofNullable(requestDto.getCouponDiscountAmount()).orElse(0L))
                .discountAmountByPoint(Optional.ofNullable(requestDto.getUsedPointDiscountAmount()).orElse(0L))
                .accumulatedPoint(Optional.ofNullable(requestDto.getAccumulatePoint()).orElse(0L))
                .build();

    }

}
