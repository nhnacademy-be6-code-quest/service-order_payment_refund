package com.nhnacademy.orderpaymentrefund.dto.order.request;

import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * CreateClientOrderRequestDto : '회원주문생성'을 위한 '주문페이지'에서 가져온 데이터 dto
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record CreateClientOrderRequestDto(

        /*
         * 페이지에서 가져 올 정보
         * 1. 상품 정보 - 주문 상품 정보, 옵션상품 정보
         * 2. 쿠폰 할인 정보 - 상품권 사용 정보
         * 3. 가격 정보 - 상품 총 금액, 쿠폰 할인 금액, 포인트 사용 금액, 배송비 정보, 결제할 금액 정보
         * 4. 주문자 정보 - 이름, 전화번호, 주소, 우편번호
         * 5. 배송날짜 정보 - 지정한 배송날짜 정보
         * 6. 결제 방식 정보 - 어떤 수단으로 결제할 것인지?
         * 7. 적립 정보 - 적립금액
         */

        /**
         * @param orderedProductAndOptionProductPairDtoList 주문된 상품과 옵션상품 쌍 리스트
         * @param couponId 쿠폰 선택 시, 할인에 적용될 쿠폰 아이디
         * @param shippingFee 배송비
         * @param productTotalAmount 상품 총 금액
         * @param payAmount 최종 결제 금액
         * @param couponDiscountAmount 쿠폰 할인 금액
         * @param usedPointDiscountAmount 포인트 사용 금액
         * @param orderedPersonName 주문자이름
         * @param phoneNumber 핸드폰 번호
         * @param totalAddress 배송지(전체이름)
         * @param designatedDeliveryDate 지정 배송일
         * @param paymentMethod 결제 수단
         * @param accumulatedPoint 적립금
         **/
        List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList,
        Long couponId,
        int shippingFee, // 배송비
        long productTotalAmount, // 상품 총 금액
        long payAmount, // 최종결제금액
        Long couponDiscountAmount, // 쿠폰 할인 금액
        Long usedPointDiscountAmount, // 포인트 사용 금액
        String orderedPersonName, // 주문자이름
        String phoneNumber, // 주문자 핸드폰 번호
        String totalAddress,
        LocalDate designatedDeliveryDate,
        int paymentMethod,
        long accumulatedPoint
){
}
