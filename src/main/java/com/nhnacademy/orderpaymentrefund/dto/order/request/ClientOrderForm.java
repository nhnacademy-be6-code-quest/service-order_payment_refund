package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ClientOrderForm {

    List<OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Long couponId; // 적용한 쿠폰
    Long pointPolicyId; // 포인트 적립 정책
    Integer shippingFee; // 배송비
    Long productTotalAmount; // 상품 총 금액(포인트 및 쿠폰 할인 전)
    Long payAmount; // 최종 결제 금액
    Long couponDiscountAmount; // 쿠폰 할인 금액
    Long usedPointDiscountAmount; // 포인트 사용 금액
    String orderedPersonName; // 주문자 이름
    String phoneNumber; // 주문자 핸드폰 번호
    String addressNickname; // 배송지 별칭
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 주소(주소,상세주소)
    Boolean useDesignatedDeliveryDate; // 배송날짜 지정 여부
    LocalDate designatedDeliveryDate; // 배송날짜 지정
    Integer paymentMethod; // 결제 방식
    Long accumulatePoint; // 예상 적립금

    @Builder
    public ClientOrderForm(String orderedPersonName){
        this.orderedPersonName = orderedPersonName;
    }

    public void addOrderDetailDtoItem(OrderDetailDtoItem orderDetailDtoItem){
        if(this.orderDetailDtoItemList == null){
            this.orderDetailDtoItemList = new ArrayList<>();
        }
        this.orderDetailDtoItemList.add(orderDetailDtoItem);
    }

    @NoArgsConstructor
    @Getter
    public static class OrderDetailDtoItem{
        Long productId; // 상품 아이디
        String productName; // 상품 이름
        Long quantity; // 수량
        List<Long> categoryIdList; // 상품의 카테고리
        Long bookId; // 상품의 책 아이디 TODO 추후 삭제될 예정
        Long productSinglePrice; // 상품 단품 가격

        Boolean usePackaging; // 포장 선택 여부
        Long optionProductId; // 옵션 상품 아이디
        String optionProductName; // 옵션 상품 이름
        Long optionProductSinglePrice; // 옵션 상품 단품 가격
        Long optionQuantity = 1L;

        @Builder
        public OrderDetailDtoItem(Long productId, String productName, Long quantity, List<Long> categoryIdList, Long bookId, Long productSinglePrice){
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.categoryIdList = categoryIdList;
            this.bookId = bookId;
            this.productSinglePrice = productSinglePrice;
        }
    }

}
