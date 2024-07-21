package com.nhnacademy.orderpaymentrefund.dto.order.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
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
    Integer paymentMethod; // 결제 방식

    @NotNull
    Long accumulatePoint; // 예상 적립금

    @NotNull
    String tossOrderId; // uuid

//    @NotNull
//    long clientId; // 주문한 회원 아이디

    @Builder
    @SuppressWarnings("java:S107") // be sure this construct
    public ClientOrderCreateForm(Long couponId, Integer shippingFee, Long productTotalAmount,
        Long payAmount, Long couponDiscountAmount, Long usedPointDiscountAmount,
        String orderedPersonName, String phoneNumber, String deliveryAddress,
        Boolean useDesignatedDeliveryDate, LocalDate designatedDeliveryDate,
        Integer paymentMethod, Long accumulatePoint, String tossOrderId
    ) {
        this.couponId = couponId;
        this.shippingFee = shippingFee;
        this.productTotalAmount = productTotalAmount;
        this.payAmount = payAmount;
        this.couponDiscountAmount = couponDiscountAmount;
        this.usedPointDiscountAmount = usedPointDiscountAmount;
        this.orderedPersonName = orderedPersonName;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.useDesignatedDeliveryDate = useDesignatedDeliveryDate;
        this.designatedDeliveryDate = designatedDeliveryDate;
        this.paymentMethod = paymentMethod;
        this.accumulatePoint = accumulatePoint;
        this.tossOrderId = tossOrderId;
    }

    public void addOrderDetailDtoItem(OrderDetailDtoItem orderDetailDtoItem) {
        if (orderDetailDtoItemList == null) {
            orderDetailDtoItemList = new ArrayList<>();
        }
        this.orderDetailDtoItemList.add(orderDetailDtoItem);
    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class OrderDetailDtoItem {

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

        Boolean usePackaging; // 포장 선택 여부

        Long optionProductId; // 옵션 상품 아이디

        String optionProductName; // 옵션 상품 이름

        Long optionProductSinglePrice; // 옵션 상품 단품 가격

        Long optionQuantity = 1L;

        @Builder
        public OrderDetailDtoItem(Long productId, String productName, Long quantity,
            List<Long> categoryIdList, Boolean packableProduct, Long productSinglePrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.categoryIdList = categoryIdList;
            this.packableProduct = packableProduct;
            this.productSinglePrice = productSinglePrice;
        }

    }

}
