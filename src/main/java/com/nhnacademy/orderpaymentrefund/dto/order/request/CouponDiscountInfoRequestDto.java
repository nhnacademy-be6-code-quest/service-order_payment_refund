package com.nhnacademy.orderpaymentrefund.dto.order.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CouponDiscountInfoRequestDto {

    List<OrderItem> orderItemList;
    Long productAndOptionTotalPrice; // 상품 + 옵션상품 합

    @NoArgsConstructor
    @Getter
    public static class OrderItem{
        Long productId; // 주문한 상품 아이디 (옵션 상품 제외)
        Long quantity; // 수량
        List<Long> categoryIdList; // 카테고리
    }

}
