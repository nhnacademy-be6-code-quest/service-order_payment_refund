package com.nhnacademy.orderpaymentrefund.dto.order.field;

import lombok.Builder;

/**
 * OrderPriceDto : '주문페이지'에서 넘어온 '금액' 정보를 모두 담고 있는 Dto
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record ClientOrderPriceInfoDto(
        /**
         * @param shippingFee 배송비. '배송정책'에 따른 무료배송 최소 금액 기준치 이상이라면 0원
         * @param productTotalAmount 상품 전체 가격. sum(메인상품 + 옵션상품)
         * @param payAmount 고객이 지불해야할 금액
         * @param couponDiscountAmount 쿠폰 할인 금액
         * @param usedPointDiscountAmount 포인트금액
         **/
        int shippingFee, // 배송비
        long productTotalAmount, // 상품 총 금액
        long payAmount, // 최종결제금액
        Long couponDiscountAmount, // 쿠폰 할인 금액
        Long usedPointDiscountAmount // 포인트 사용 금액
){
}
