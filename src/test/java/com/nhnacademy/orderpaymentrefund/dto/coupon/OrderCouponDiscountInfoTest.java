package com.nhnacademy.orderpaymentrefund.dto.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderCouponDiscountInfo;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderCouponDiscountInfoTest {

    private OrderCouponDiscountInfo orderCouponDiscountInfo;

    @BeforeEach
    public void setUp() {
        // 초기 설정
        orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
            .couponId(1L)
            .isApplicable(true)
            .discountTotalAmount(1000L)
            .build();
    }

    @Test
    public void testBuilder() {
        // 빌더를 통해 설정된 값 확인
        assertThat(orderCouponDiscountInfo.getCouponId()).isEqualTo(1L);
        assertThat(orderCouponDiscountInfo.getIsApplicable()).isTrue();
        assertThat(orderCouponDiscountInfo.getDiscountTotalAmount()).isEqualTo(1000L);
    }

    @Test
    public void testUpdateIsApplicable() {
        // 쿠폰 적용 가능 여부 업데이트 테스트
        orderCouponDiscountInfo.updateIsApplicable(false);
        assertThat(orderCouponDiscountInfo.getIsApplicable()).isFalse();
    }

    @Test
    public void testUpdateNotApplicableDescription() {
        // 사용 불가 사유 업데이트 테스트
        String description = "쿠폰 만료됨";
        orderCouponDiscountInfo.updateNotApplicableDescription(description);
        assertThat(orderCouponDiscountInfo.getNotApplicableDescription()).isEqualTo(description);
    }

}
