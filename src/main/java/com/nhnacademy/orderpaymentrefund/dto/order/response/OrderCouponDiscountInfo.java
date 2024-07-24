package com.nhnacademy.orderpaymentrefund.dto.order.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderCouponDiscountInfo {

    private Long couponId;
    private Boolean isApplicable; // 쿠폰 적용 가능 여부
    private String notApplicableDescription; // 사용 불가 사유

    private Map<Long, Long> productPriceInfoAfterCouponDiscountList; // 상품아이디 - 할인후단품가격
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

    public void addProductPriceInfo(Long productId, Long productSinglePriceAfterCouponDiscount) {
        if(productPriceInfoAfterCouponDiscountList == null){
            productPriceInfoAfterCouponDiscountList = new HashMap<>();
        }
        productPriceInfoAfterCouponDiscountList.put(productId, productSinglePriceAfterCouponDiscount);
    }

}
