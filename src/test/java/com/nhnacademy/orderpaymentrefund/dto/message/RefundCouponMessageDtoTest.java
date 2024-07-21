package com.nhnacademy.orderpaymentrefund.dto.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RefundCouponMessageDtoTest {

    @Test
    void testDefaultConstructor() {
        RefundCouponMessageDto dto = new RefundCouponMessageDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getCouponId()).isZero(); // 기본값은 0으로 초기화됩니다.
    }

    @Test
    void testAllArgsConstructor() {
        long couponId = 12345L;
        RefundCouponMessageDto dto = new RefundCouponMessageDto(couponId);
        assertThat(dto).isNotNull();
        assertThat(dto.getCouponId()).isEqualTo(couponId);
    }

    @Test
    void testGetter() {
        long couponId = 12345L;
        RefundCouponMessageDto dto = new RefundCouponMessageDto(couponId);
        assertThat(dto.getCouponId()).isEqualTo(couponId);
    }
}
