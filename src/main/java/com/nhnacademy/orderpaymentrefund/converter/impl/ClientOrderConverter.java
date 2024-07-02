package com.nhnacademy.orderpaymentrefund.converter.impl;

import com.nhnacademy.orderpaymentrefund.converter.Converter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClientOrderConverter implements Converter<Order, CreateClientOrderRequestDto> {

    @Override
    public Order dtoToEntity(CreateClientOrderRequestDto requestDto, Object ... objects) {

        if(objects.length == 1 && objects[0] instanceof Long) {

            StringBuilder address = new StringBuilder();
            address.append(requestDto.clientOrdererInfoDto().zipCode());
            address.append(", ");
            address.append(requestDto.clientOrdererInfoDto().address());
            address.append(", ");
            address.append(requestDto.clientOrdererInfoDto().detailAddress());

            return Order.clientOrderBuilder()
                    .clientId((Long) objects[0])
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
        else {
            throw new IllegalArgumentException("dto 인자를 제외한 인자는 long 타입의 clientId만 올 수 있습니다.");
        }

    }

}
