package com.nhnacademy.orderpaymentrefund.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 토스 페이먼츠 결제 승인 요청에 필요한 정보를 담고 있는 DTO 입니다. 모든 값들은 TossPayments 에서 보내 주는 Success URL 의 Query
 * Parameter 를 통해 받습니다. (주문에서 받아오는 값들이 아닙니다.)
 *
 * @author 김채호
 * @version 1.0
 */
@Builder
@Getter
public class TossPaymentsRequestDto {

    @NotNull
    private String paymentKey;  // 토스에서 각 결제마다 부여하는 고유 값입니다.

    @NotNull
    private String orderId;     // 주문에서 받아 토스에 제공한 tossOrderId 입니다. 토스는 내가 제공한 값을 다시 돌려줍니다.

    @NotNull
    private long amount;        // 주문에서 받아 토스에 제공한 순수 결제 금액입니다. 토스는 내가 제공한 값을 다시 돌려줍니다.
}
