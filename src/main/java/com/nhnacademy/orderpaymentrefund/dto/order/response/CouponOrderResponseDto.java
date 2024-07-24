package com.nhnacademy.orderpaymentrefund.dto.order.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CouponOrderResponseDto {
    long couponId;
    CouponPolicyDto couponPolicyDto;
    ProductCoupon productCoupon;
    CategoryCoupon categoryCoupon;

    @Setter
    @NoArgsConstructor
    @Getter
    public static class CouponPolicyDto {
        String couponPolicyDescription;
        String discountType;
        long discountValue;
        long minPurchaseAmount;
        long maxDiscountAmount;    }
    @Setter
    @Getter
    @NoArgsConstructor
    public static class ProductCoupon {
        Long productId;
    }
    @Setter
    @Getter
    @NoArgsConstructor
    public static class CategoryCoupon {
        Long productCategoryId;
    }

}