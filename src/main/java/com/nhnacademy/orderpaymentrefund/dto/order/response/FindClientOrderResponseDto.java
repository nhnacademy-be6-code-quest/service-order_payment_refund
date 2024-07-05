package com.nhnacademy.orderpaymentrefund.dto.order.response;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * FindClientOrderResponseDto : 회원의 주문 내역 조회 시 보여줄 데이터 Dto. Page로 보내기
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record FindClientOrderResponseDto (

        /**
         * @param orderId 주문 아이디
         * @param orderStatus 주문 상태
         * @param orderDate 주문 일자
         * @param designatedDeliveryDate 지정 날짜
         * @param paymentMethod 결제 수단
         * @param deliveryStartDate 출고 일자
         * @param deliveryAddress 배송 주소지
         * @param clientOrderPriceInfoDto 주문과 관련된 금액 정보(배송비, 상품 총 액, 할인금액, 적립금액, 결제금액 등)
         * @param orderedProductAndOptionProductPairDtoList 주문 상품 정보 (옵션상품 포함)
         **/

        long orderId,
        OrderStatus orderStatus,
        LocalDateTime orderDateTime,
        LocalDate designatedDeliveryDate,
        String paymentMethod,
        LocalDate deliveryStartDate,
        String deliveryAddress,
        ClientOrderPriceInfoDto clientOrderPriceInfoDto,
        String phoneNumber,
        List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList
){
}
