package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderCouponDiscountInfo {

    private Long couponId;

    // 쿠폰 기본 정보
    private String couponPolicyDescription; // 쿠폰 정책 설명
    private String discountUnit; // 원 or %
    private Long minPurchaseAmount; // 할인 대산 주문 상품들의 총 합의 최소 주문 금액
    private Long maxDiscountAmount; // 최대 할인 금액
    private Long discountValue;

    // 쿠폰 할인 정보
    private Boolean isApplicable; // 쿠폰 적용 가능 여부
    private String notApplicableDescription; // 사용 불가 사유
    private Long discountTotalAmount;

    @Builder
    public OrderCouponDiscountInfo(Long couponId, Boolean isApplicable, Long discountTotalAmount) {
        this.couponId = couponId;
        this.isApplicable = isApplicable;
        this.discountTotalAmount = discountTotalAmount;
    }

    public void updateIsApplicable(Boolean isApplicable) {
        this.isApplicable = isApplicable;
    }

    public void updateNotApplicableDescription(String notApplicableDescription){
        this.notApplicableDescription = notApplicableDescription;
    }

    public void updateCouponInfo(String couponPolicyDescription, String discountUnit, Long minPurchaseAmount, long maxDiscountAmount, long discountValue){
        this.couponPolicyDescription = couponPolicyDescription;
        this.discountUnit = discountUnit;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.discountValue = discountValue;
    }

}
