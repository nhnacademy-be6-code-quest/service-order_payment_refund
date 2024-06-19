package com.nhnacademy.order.dto.order.response.client;

import com.nhnacademy.order.dto.order.response.field.OrderedProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 회원이 주문한 주문 내역 건
public class ClientAllOrderGetResponseDto {
    private ZonedDateTime orderDate; // 주문날짜
    private String address; // 배송지
    private List<OrderedProductDto> orderedProductDtoList; // 주문한 상품정보들
    private long totalProductPrice; // 상품 총 금액
    private long pointUsageAmount; // 사용 포인트 금액
    private String couponPolicyDescription; // 사용한 쿠폰 정책 이름
    private long couponDiscountAmount; // 쿠폰 할인된 금액
    private long shippingFee; // 배송비
    private long totalPayAmount; // 결제된 금액 or 결제할 금액
}
