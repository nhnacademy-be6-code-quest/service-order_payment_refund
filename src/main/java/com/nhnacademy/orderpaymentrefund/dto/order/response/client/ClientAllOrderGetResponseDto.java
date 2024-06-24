package com.nhnacademy.orderpaymentrefund.dto.order.response.client;

import com.nhnacademy.orderpaymentrefund.dto.order.response.field.OrderedProductDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
// 회원이 주문한 주문 내역 건
public record ClientAllOrderGetResponseDto (
    LocalDateTime orderDate, // 주문날짜
    String address, // 배송지
    List<OrderedProductDto> orderedProductDtoList, // 주문한 상품정보들
    long totalPrice, // 상품 총 금액
    Long pointUsageAmount, // 사용 포인트 금액
    String couponPolicyDescription, // 사용한 쿠폰 정책 이름
    Long couponDiscountAmount, // 쿠폰 할인된 금액
    long shippingFee, // 배송비
    Long totalPayAmount // 결제된 금액 or 결제할 금액
)
{

}
