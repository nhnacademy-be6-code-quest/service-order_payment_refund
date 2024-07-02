package com.nhnacademy.orderpaymentrefund.dto.order.response;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.NonClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FindNonClientOrderResponseDto (

        /**
         * @param orderId 주문 아이디
         * @param orderStatus 주문 상태
         * @param orderDate 주문 일자
         * @param designatedDeliveryDate 지정 날짜
         * @param paymentMethod 결제 수단
         * @param deliveryStartDate 출고 일자
         * @param deliveryAddress 배송 주소지
         * @param nonClientOrderPriceInfoDto 주문과 관련된 금액 정보(상품 총 액, 할인금액, 적립금액, 결제금액 등),
         * @param phoneNumber 핸드폰 번호
         * @param orderedProductAndOptionProductPairDtoList 주문 상품 정보 (옵션상품 포함)
         * @param ordererName 주문자 이름
         **/

        long orderId,
        OrderStatus orderStatus,
        LocalDateTime orderDateTime,
        LocalDate designatedDeliveryDate,
        String paymentMethod,
        LocalDate deliveryStartDate,
        String deliveryAddress,
        NonClientOrderPriceInfoDto nonClientOrderPriceInfoDto,
        String phoneNumber,
        List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList,
        String ordererName
){
}
