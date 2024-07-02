package com.nhnacademy.orderpaymentrefund.dto.order.response;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * FindClientOrderResponseDto : 회원의 주문 내역 조회 시 필요한 데이터 Dto
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record FindClientOrderResponseDto (

        /**
         * @param orderId 주문 아이디
         * @param orderStatus 주문 상태
         * @param orderDate 주문 일자
         * @param isDesignatedDeliveryDate 배송일자 지정 여부
         * @param deliveryStartDate 출고일자
         * @param deliveryAddress 배송 주소지
         * @param clientOrderPriceInfoDto 주문과 관련된 금액 정보(상품 총 액, 할인금액, 적립금액, 결제금액 등)
         * @param orderedProductAndOptionProductPairDtoList 주문 상품 정보 (옵션상품 포함)
         **/

        long orderId,
        OrderStatus orderStatus,
        LocalDate orderDate,
        LocalDate designatedDeliveryDate,
        LocalDate deliveryStartDate,
        String deliveryAddress,
        ClientOrderPriceInfoDto clientOrderPriceInfoDto,
        List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList
){
}
