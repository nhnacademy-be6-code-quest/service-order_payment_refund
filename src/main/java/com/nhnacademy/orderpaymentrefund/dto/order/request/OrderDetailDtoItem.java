package com.nhnacademy.orderpaymentrefund.dto.order.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class OrderDetailDtoItem {
    @NotNull
    Long productId; // 상품 아이디

    @NotNull
    String productName; // 상품 이름

    @NotNull
    Long quantity; // 수량

    List<Long> categoryIdList; // 상품의 카테고리

    @NotNull
    Long productSinglePrice; // 상품 단품 가격

    @NotNull
    Boolean packableProduct; // 포장 가능 상품 여부

    @NotNull
    Boolean usePackaging; // 포장 선택 여부

    Long optionProductId; // 옵션 상품 아이디

    String optionProductName; // 옵션 상품 이름

    Long optionProductSinglePrice; // 옵션 상품 단품 가격

    Long optionQuantity;
}
