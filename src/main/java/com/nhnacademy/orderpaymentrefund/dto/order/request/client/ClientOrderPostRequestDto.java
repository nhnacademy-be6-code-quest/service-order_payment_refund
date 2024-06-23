package com.nhnacademy.orderpaymentrefund.dto.order.request.client;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.request.field.OrderDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
public record ClientOrderPostRequestDto (
    long clientId,
    List<OrderDetailDto> orderDetailDtoList,
    ZonedDateTime orderDate,
    ZonedDateTime deliveryDate,
    OrderStatus orderStatus,
    long totalPrice,
    int shippingFee,
    String phoneNumber,
    String deliveryAddress
)
{

}