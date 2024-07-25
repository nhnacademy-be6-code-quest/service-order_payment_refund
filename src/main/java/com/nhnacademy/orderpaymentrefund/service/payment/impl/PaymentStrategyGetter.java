package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.service.payment.PaymentStrategy;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyGetter {

    private final Map<String, PaymentStrategy> paymentStrategyMap;

    public PaymentStrategy getPaymentStrategy(String paymentType) {
        return paymentStrategyMap.get(paymentType);
        //if 로 에외처리
    }

}
