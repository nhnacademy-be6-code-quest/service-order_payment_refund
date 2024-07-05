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

        StringBuilder address = new StringBuilder();
        address.append(requestDto.clientOrdererInfoDto().zipCode());
        address.append(", ");
        address.append(requestDto.clientOrdererInfoDto().address());
        address.append(", ");
        address.append(requestDto.clientOrdererInfoDto().detailAddress());

        return Order.clientOrderBuilder()
                .clientId(clientId)
                .couponId(requestDto.couponId())
                .pointPolicyId(requestDto.pointPolicyId())
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(requestDto.clientOrderPriceInfoDto().productTotalAmount())
                .shippingFee(requestDto.clientOrderPriceInfoDto().shippingFee())
                .designatedDeliveryDate(requestDto.designatedDeliveryDate())
                .phoneNumber(requestDto.clientOrdererInfoDto().phoneNumber())
                .deliveryAddress(address.toString())
                .discountAmountByCoupon(Optional.ofNullable(requestDto.clientOrderPriceInfoDto().couponDiscountAmount()).orElse(0L))
                .discountAmountByPoint(Optional.ofNullable(requestDto.clientOrderPriceInfoDto().usedPointDiscountAmount()).orElse(0L))
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
