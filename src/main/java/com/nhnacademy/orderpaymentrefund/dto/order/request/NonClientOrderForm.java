package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class NonClientOrderForm {

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
    String tossOrderId; // 토스 주문

}
