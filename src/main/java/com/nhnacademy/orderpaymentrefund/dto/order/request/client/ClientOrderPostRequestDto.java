package com.nhnacademy.orderpaymentrefund.dto.order.request.client;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.request.field.OrderDetailDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientOrderPostRequestDto {
    private long clientId;
    private List<OrderDetailDto> orderDetailDtoList;
    private ZonedDateTime orderDate;
    private ZonedDateTime deliveryDate;
    private OrderStatus orderStatus;
    private long totalPrice;
    private int shippingFee;
    private String phoneNumber;
    private String deliveryAddress;
}