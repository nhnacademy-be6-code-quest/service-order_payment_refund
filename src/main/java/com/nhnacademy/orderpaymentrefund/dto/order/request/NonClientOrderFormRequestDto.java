package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class NonClientOrderFormRequestDto {
    List<OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Integer shippingFee; // 배송비
    Long productTotalAmount; // 상품 총 금액
    Long payAmount; // 최종 결제 금액
    String orderedPersonName; // 비회원 주문자 이름
    String email; // 이메일
    String phoneNumber; // 비회원 주문자 전화번호
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 배송 주소
    String deliveryDetailAddress; // 배송 상세주소
    Boolean useDesignatedDeliveryDate; // 배송날짜 지정 여부
    LocalDate designatedDeliveryDate; // 배송날짜 지정
    Integer paymentMethod; // 결제 방식
    String orderPassword; // 주문 비밀번호

    @NoArgsConstructor
    @Getter
    public static class OrderDetailDtoItem{
        Long productId; // 상품 아이디
        String productName; // 상품 이름
        Long quantity; // 수량
        Long productSinglePrice; // 상품 단품 가격
        Boolean packableProduct; // 포장가능 상품 여부

        Boolean usePackaging; // 포장 선택 여부
        Long optionProductId; // 옵션 상품 아이디
        String optionProductName; // 옵션 상품 이름
        Long optionProductSinglePrice; // 옵션 상품 단품 가격
        Long optionQuantity = 1L;

        @Builder
        public OrderDetailDtoItem(Long productId, String productName, Long quantity, Long productSinglePrice, Boolean packableProduct){
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.packableProduct = packableProduct;
            this.productSinglePrice = productSinglePrice;
        }
    }
}
