package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethod;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class PaymentResponseDto {
    private Long paymentId;
    private Order order;
    private PaymentMethod paymentMethod;
    private Long couponId;
    private Long payAmount;
    private LocalDateTime payTime;
}