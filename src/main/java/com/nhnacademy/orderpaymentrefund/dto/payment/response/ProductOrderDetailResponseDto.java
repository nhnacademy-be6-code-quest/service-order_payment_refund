package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOrderDetailResponseDto {
    long productId;
    long quantity;
    long pricePerProduct;
    long productCategoryId; // 상품 쪽에 API 요청해야 함.
}