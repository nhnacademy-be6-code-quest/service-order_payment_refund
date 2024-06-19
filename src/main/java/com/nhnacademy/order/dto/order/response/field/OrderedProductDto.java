package com.nhnacademy.order.dto.order.response.field;

import com.nhnacademy.order.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderedProductDto {
    private long productId; // 주문한 상품 아이디
    private String productName; // 주문한 상품 이름
    private String productImagePath; // 주문한 상품 이미지
    private long productPrice; // 주문당시 상품 단품 가격
    private long quantity; // 상품 수량
    private OrderStatus orderStatus; // 주문 상태
    private Long packageId; // 포장지 아이디
    private String packageName; // 포장지 이름
    private Long packagePrice; // 포장지 가격
}
