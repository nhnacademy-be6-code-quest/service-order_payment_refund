package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PaymentRefundResponseDto {
    Long paymentId;
    String tossPaymentKey;
    String orderStatus;
}
