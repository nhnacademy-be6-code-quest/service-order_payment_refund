package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data // Getter, Setter, equals & hashCode, toString, NoArgs, AllArgs ...
public class PaymentRequestDto {
    @NotNull
    private Long orderId;                   // 주문 아이디

    private Long paymentMethodId;           // 결제 수단의 아이디

    private Long couponId;                  // 이번 결제에서 사용할 쿠폰의 아이디

    private Long payAmount;                 // 최종 결제한 금액
}
