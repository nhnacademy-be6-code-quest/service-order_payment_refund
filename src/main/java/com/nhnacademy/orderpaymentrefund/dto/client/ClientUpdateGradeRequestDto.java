package com.nhnacademy.orderpaymentrefund.dto.client;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientUpdateGradeRequestDto {
    private Long clientId;
    private Long payment; // 3개월 순수 결제금액 총합
}