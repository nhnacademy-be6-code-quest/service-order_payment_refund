package com.nhnacademy.payment.dto;

import com.nhnacademy.payment.domain.PaymentMethod;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDto {
    private Long paymentId;
    private Long orderId;
    private LocalDateTime payTime;
    private Long payAmount;
    private Long clientDeliveryAddressId;
    private PaymentMethod paymentMethod;
    private Long couponId;
}
