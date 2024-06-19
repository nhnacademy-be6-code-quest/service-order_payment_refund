package com.nhnacademy.payment.dto;

import com.nhnacademy.payment.domain.Coupon;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 새로운 결제가 생성될 때 사용자에게 보여주는 정보들입니다.
 *
 * @author Virtus_Chae
 * @version 1.0
 */

public record PaymentCreateRequestGet(
    // 1. 사용자가 가지고 있는 포인트의 총량
    @NotNull
    Long point,

    // 2. 사용자가 가지고 있는 쿠폰의 리스트
//        @NotNull
    List<Coupon> couponList
) {

}