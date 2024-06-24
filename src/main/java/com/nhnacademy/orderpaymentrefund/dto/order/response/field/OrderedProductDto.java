package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import lombok.Builder;

@Builder
public record OrderedProductDto (
    long productId, // 주문한 상품 아이디
    String productName, // 주문한 상품 이름
    String productImagePath, // 주문한 상품 이미지
    long productPrice, // 주문당시 상품 단품 가격
    long quantity, // 상품 수량
    OrderStatus orderStatus, // 주문 상태
    Long packageId, // 포장지 아이디
    String packageName, // 포장지 이름
    Long packagePrice // 포장지 가격
)
{

}
