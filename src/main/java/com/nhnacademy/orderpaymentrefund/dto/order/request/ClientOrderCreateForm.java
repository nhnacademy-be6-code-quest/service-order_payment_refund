package com.nhnacademy.orderpaymentrefund.dto.order.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ClientOrderCreateForm {

    // 주문 상품 및 배송지 정보
    List<OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Integer shippingFee; // 회원 배송비
    Long productTotalAmount; // 상품 총 금액
    Long orderTotalAmount; // 주문 총 금액 (상품 총 금액 + 배송비)
    String phoneNumber; // 주문자 핸드폰 번호
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 주소(주소 + 상세주소)
    Boolean useDesignatedDeliveryDate = false; // 배송날짜 지정 여부
    LocalDate designatedDeliveryDate; // 배송날짜 지정
    Long totalQuantity;

    // 할인 금액(쿠폰 및 포인트) 정보
    Long payAmount; // 실제 결제할 가격. 쿠폰 할인, 포인트 사용 적용 후 금액!
    Long couponId; // 적용한 쿠폰
    Long couponDiscountAmount = 0L; // 쿠폰 할인 금액
    Long usedPointDiscountAmount = 0L; // 포인트 사용 금액

    // 적립금
    Long accumulatePoint = 0L;

    // 결제 수단 정보
    String paymentMethod; // 결제 수단 리스트

    // 주문 코드
    String orderCode;

    public void addOrderDetailDtoItem(OrderDetailDtoItem orderDetailDtoItem){
        if(this.orderDetailDtoItemList == null){
            this.orderDetailDtoItemList = new ArrayList<>();
        }
        this.orderDetailDtoItemList.add(orderDetailDtoItem);
    }

}
