package com.nhnacademy.orderpaymentrefund.dto.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto;
import org.junit.jupiter.api.Test;

class CouponOrderResponseDtoTest {
    @Test
    void testCouponOrderResponseDto() {
        CouponOrderResponseDto.CouponPolicyDto couponPolicyDto = new CouponOrderResponseDto.CouponPolicyDto();
        couponPolicyDto.setCouponPolicyDescription("10% discount on all products");
        couponPolicyDto.setDiscountType("PERCENTAGE");
        couponPolicyDto.setDiscountValue(10);
        couponPolicyDto.setMinPurchaseAmount(100);
        couponPolicyDto.setMaxDiscountAmount(50);

        CouponOrderResponseDto.ProductCoupon productCoupon = new CouponOrderResponseDto.ProductCoupon();
        productCoupon.setProductId(1L);

        CouponOrderResponseDto.CategoryCoupon categoryCoupon = new CouponOrderResponseDto.CategoryCoupon();
        categoryCoupon.setProductCategoryId(2L);

        // Create the main DTO and set its fields
        CouponOrderResponseDto couponOrderResponseDto = new CouponOrderResponseDto();
        couponOrderResponseDto.setCouponId(123L);
        couponOrderResponseDto.setCouponPolicyDto(couponPolicyDto);
        couponOrderResponseDto.setProductCoupon(productCoupon);
        couponOrderResponseDto.setCategoryCoupon(categoryCoupon);

        // Assertions to check if values are correctly set
        assertEquals(123L, couponOrderResponseDto.getCouponId());

        assertNotNull(couponOrderResponseDto.getCouponPolicyDto());
        assertEquals("10% discount on all products", couponOrderResponseDto.getCouponPolicyDto().getCouponPolicyDescription());
        assertEquals("PERCENTAGE", couponOrderResponseDto.getCouponPolicyDto().getDiscountType());
        assertEquals(10, couponOrderResponseDto.getCouponPolicyDto().getDiscountValue());
        assertEquals(100, couponOrderResponseDto.getCouponPolicyDto().getMinPurchaseAmount());
        assertEquals(50, couponOrderResponseDto.getCouponPolicyDto().getMaxDiscountAmount());

        assertNotNull(couponOrderResponseDto.getProductCoupon());
        assertEquals(1L, couponOrderResponseDto.getProductCoupon().getProductId());

        assertNotNull(couponOrderResponseDto.getCategoryCoupon());
        assertEquals(2L, couponOrderResponseDto.getCategoryCoupon().getProductCategoryId());
    }
}
