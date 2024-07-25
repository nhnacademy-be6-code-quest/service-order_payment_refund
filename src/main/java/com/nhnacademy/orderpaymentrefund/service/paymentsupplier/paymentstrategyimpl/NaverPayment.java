package com.nhnacademy.orderpaymentrefund.service.paymentsupplier.paymentstrategyimpl;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.paymentsupplier.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("naver")
public class NaverPayment implements PaymentStrategy {

    private final String naverSecretKey;

    @Override
    public PaymentsResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {
        return null;
    }

    @Override
    public void paymentRefund(long orderId, String cancelReason) {

    }
}
