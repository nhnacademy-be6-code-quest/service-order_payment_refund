package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ApprovePaymentRequestDto {
    private String orderCode;
    private String pgName; // paymentMethodTypeName
    private Map<String, String[]> reqParamMap; // 고객의 결제 완료 후 얻은 요청 파라미터 map
}
