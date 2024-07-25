package com.nhnacademy.orderpaymentrefund.dto.order.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ClientOrderCreateForm {

    @NotNull
    List<OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트

    Long couponId; // 적용한 쿠폰

    @NotNull
    Integer shippingFee; // 배송비

    @NotNull
    Long productTotalAmount; // 상품 총 금액(배송비 포함 x, 포인트 및 쿠폰 할인 전)

    @NotNull
    Long orderTotalAmount; // productTotalAmount + shippingFee

    @NotNull
    Long payAmount; // 최종 결제 금액

    Long couponDiscountAmount; // 쿠폰 할인 금액

    Long usedPointDiscountAmount; // 포인트 사용 금액

    @NotNull
    String orderedPersonName; // 주문자 이름

    @NotNull
    String phoneNumber; // 주문자 핸드폰 번호

    @NotNull
    String deliveryAddress; // 주소(주소,상세주소)

    Boolean useDesignatedDeliveryDate; // 배송날짜 지정 여부

    LocalDate designatedDeliveryDate; // 배송날짜 지정

    @NotNull
    String paymentMethod; // 결제 방식

    @NotNull
    Long accumulatePoint; // 예상 적립금

    @NotNull
    String orderCode; // uuid

}
