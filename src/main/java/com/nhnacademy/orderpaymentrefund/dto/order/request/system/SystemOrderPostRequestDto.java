package com.nhnacademy.orderpaymentrefund.dto.order.request.system;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.request.field.OrderDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SystemOrderPostRequestDto {
    private List<OrderDetailDto> orderDetailDtoList;
    private ZonedDateTime orderDate;
    private ZonedDateTime deliveryDate;
    private OrderStatus orderStatus;
    private long totalPrice;
    private long shippingPolicyId;
    private long clientId;
    private int shippingFee; // 구매당시 배송비
}