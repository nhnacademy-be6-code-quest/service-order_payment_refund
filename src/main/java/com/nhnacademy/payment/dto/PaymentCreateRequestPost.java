package com.nhnacademy.payment.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 새로운 결제가 생성될 때 사용자에게 받는 정보들입니다.
 * 결제가 실제로 실행될 때 사용됩니다.
 *
 * @author Virtus_Chae
 * @version 1.0
 */
// 새로운 결제가 생성될 때 받아야 하는 정보들
public record PaymentCreateRequestPost(
    // 1. 결제할 때 사용할 포인트
    @NotNull
    Long point,

    // 2. 결제할 때 사용할 쿠폰
//        @NotNull
    Long couponId,

    // 3. 결제 방식
    @NotNull
    Long paymentMethodId
) {

}