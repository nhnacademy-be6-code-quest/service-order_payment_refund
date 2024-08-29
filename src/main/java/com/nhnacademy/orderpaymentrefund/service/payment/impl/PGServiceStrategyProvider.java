package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGServiceStrategyProvider {

    private final Map<String, PGServiceStrategy> pgServiceStrategyMap;

    public PGServiceStrategy getPaymentStrategy(String pgName) {
        pgName = pgName.toLowerCase();
        return pgServiceStrategyMap.get(pgName);
        //if 로 에외처리
    }

}
