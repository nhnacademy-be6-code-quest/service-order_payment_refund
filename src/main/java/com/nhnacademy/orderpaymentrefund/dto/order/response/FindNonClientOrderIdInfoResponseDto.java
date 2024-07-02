package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * FindNonClientOrderIdInfoResponseDto : 비회원이 주문 아이디 찾기를 시도했을 때, 결과로 나오는 응답 dto
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record FindNonClientOrderIdInfoResponseDto(

        /**
         * @param orderId 주문 아이디
         * @param orderDateTime 주문 일시
         **/

        long orderId,
        LocalDateTime orderDateTime
) {
}
