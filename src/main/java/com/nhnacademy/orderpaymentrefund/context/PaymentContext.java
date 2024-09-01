package com.nhnacademy.orderpaymentrefund.context;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;
import org.springframework.stereotype.Component;

@Component
public class PaymentContext {

    private static final ThreadLocal<String> paymentKey = new ThreadLocal<>();
    private static final ThreadLocal<String> paymentMethodName = new ThreadLocal<>(); // PG서비스를 통해 사용자가 선택한 결제 수단(예: 간편결제, 삼성페이, 계좌이체, 카드 등..)

    public void setPaymentKey(String paymentKey) {
        PaymentContext.paymentKey.set(paymentKey);
    }

    public void setPaymentMethodName(String paymentMethodName) {
        PaymentContext.paymentMethodName.set(paymentMethodName);
    }

    public String getPaymentKey() {
        if (paymentKey.get() == null) {
            throw new BadRequestExceptionType("PaymentKey가 존재하지 않습니다.");
        }
        return paymentKey.get();
    }

    public String getPaymentMethodName() {
        if (paymentMethodName.get() == null) {
            throw new BadRequestExceptionType("paymentMethodName이 존재하지 않습니다.");
        }
        return paymentMethodName.get();
    }

}
