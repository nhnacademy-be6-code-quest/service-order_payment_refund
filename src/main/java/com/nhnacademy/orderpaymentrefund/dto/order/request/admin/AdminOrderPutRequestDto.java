package com.nhnacademy.orderpaymentrefund.dto.order.request.admin;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
public record AdminOrderPutRequestDto(// 관리자가 주문에 대해 수정할 사항들.
        OrderStatus orderStatus
){ // record는 기본적으로 '모든 필드를 받는 생성자'를 만듦. 근데! record에 아래처럼 아무런 생성자도 개발자가 작성하지 않으면 '아무 필드도 받지않는 기본생성자'를 만듦

}