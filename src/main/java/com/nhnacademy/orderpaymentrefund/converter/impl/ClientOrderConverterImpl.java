package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.ClientOrderConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClientOrderConverterImpl implements ClientOrderConverter {

    public Order dtoToEntity(CreateClientOrderRequestDto requestDto, long clientId) {

        return Order.clientOrderBuilder()
                .clientId(clientId)
                .couponId(requestDto.couponId())
                .pointPolicyId(requestDto.pointPolicyId())
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(requestDto.productTotalAmount())
                .shippingFee(requestDto.shippingFee())
                .designatedDeliveryDate(requestDto.designatedDeliveryDate())
                .phoneNumber(requestDto.phoneNumber())
                .deliveryAddress(requestDto.totalAddress())
                .discountAmountByCoupon(Optional.ofNullable(requestDto.couponDiscountAmount()).orElse(0L))
                .discountAmountByPoint(Optional.ofNullable(requestDto.usedPointDiscountAmount()).orElse(0L))
                .accumulatedPoint(requestDto.accumulatedPoint())
                .build();

    }

    public FindClientOrderResponseDto entityToDto(Order order, String paymentMethod, ClientOrderPriceInfoDto clientOrderPriceInfoDto,
                                                  List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList){
        return FindClientOrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .orderDateTime(order.getOrderDatetime())
                .designatedDeliveryDate(order.getDesignatedDeliveryDate())
                .paymentMethod(paymentMethod)
                .deliveryStartDate(order.getDeliveryStartDate())
                .deliveryAddress(order.getDeliveryAddress())
                .clientOrderPriceInfoDto(clientOrderPriceInfoDto)
                .phoneNumber(order.getPhoneNumber())
                .orderedProductAndOptionProductPairDtoList(orderedProductAndOptionProductPairDtoList)
                .build();
    }

}
