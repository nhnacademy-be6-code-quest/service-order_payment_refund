package com.nhnacademy.payment.dto;

import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.payment.domain.PaymentMethod;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDto {
    private Long paymentId;
    private Order order;
    private PaymentMethod paymentMethod;
    private Long couponId;
    private Long payAmount;
    private LocalDateTime payTime;
}