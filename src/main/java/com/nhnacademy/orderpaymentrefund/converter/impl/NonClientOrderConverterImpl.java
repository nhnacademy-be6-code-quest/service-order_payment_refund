package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.NonClientOrderConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.order.field.NonClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class NonClientOrderConverterImpl implements NonClientOrderConverter {

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

    public FindNonClientOrderResponseDto entityToDto(Order order, String paymentMethod, NonClientOrderPriceInfoDto nonClientOrderPriceInfoDto,
                                                     List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList) {
        return FindNonClientOrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .orderDateTime(order.getOrderDatetime())
                .designatedDeliveryDate(order.getDesignatedDeliveryDate())
                .paymentMethod(paymentMethod)
                .deliveryStartDate(order.getDeliveryStartDate())
                .deliveryAddress(order.getDeliveryAddress())
                .nonClientOrderPriceInfoDto(nonClientOrderPriceInfoDto)
                .phoneNumber(order.getPhoneNumber())
                .orderedProductAndOptionProductPairDtoList(orderedProductAndOptionProductPairDtoList)
                .ordererName(order.getNonClientOrdererName())
                .build();
    }

}
