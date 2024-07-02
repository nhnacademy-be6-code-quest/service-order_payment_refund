package com.nhnacademy.orderpaymentrefund.dto.order.request;

import com.nhnacademy.orderpaymentrefund.dto.order.field.NonClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.NonClientOrdererInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * CreateNonClientOrderRequestDto : '비회원주문생성'을 위한 '주문페이지'에서 가져온 데이터 dto
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@Builder
public record CreateNonClientOrderRequestDto(

        /*
         * 페이지에서 가져 올 정보
         * 1. 상품 정보 - 주문 상품 정보, 옵션상품 정보
         * 2. 가격 정보 - 상품 총 금액, 배송비 정보, 결제할 금액 정보
         * 3. 주문자 정보 - 이름, 전화번호, 주소, 우편번호
         * 4. 배송날짜 정보 - 지정한 배송날짜 정보
         * 5. 결제 방식 정보 - 어떤 수단으로 결제할 것인지?
         */

        /**
         * @param orderedProductAndOptionProductPairDtoList 주문된 상품과 옵션상품 쌍 리스트
         * @param nonClientOrderPriceInfoDto 비회원 주문 가격 정보(배송비, 상품 전체 가격, 결제금액)
         * @param nonClientOrdererInfoDto 비회원 주문자 정보(주문 비밀번호, 이름, 이메일)
         * @param designatedDeliveryDate 지정 배송지
         * @param paymentMethod 비회원이 선택한 결제 수단
         **/

        List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList,
        NonClientOrderPriceInfoDto nonClientOrderPriceInfoDto,
        NonClientOrdererInfoDto nonClientOrdererInfoDto,
        LocalDate designatedDeliveryDate,
        int paymentMethod

){
}
