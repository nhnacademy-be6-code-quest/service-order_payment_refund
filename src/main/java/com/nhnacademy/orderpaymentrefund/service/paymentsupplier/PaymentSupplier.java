package com.nhnacademy.orderpaymentrefund.service.paymentsupplier;

import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class PaymentSupplier {
    private final Map<String, PaymentStrategy> paymentStrategyMap;
    public PaymentStrategy getSupplier (String methodType){
        PaymentStrategy paymentStrategy = paymentStrategyMap.get(methodType);
        if (Objects.isNull(paymentStrategy)){
            log.error("결재 종류가 존재하지 않습니다.");
            throw new PaymentNotFoundException("결재 종류가 존재하지않습니다.");
        }
        return paymentStrategy;
    }
}
