package com.nhnacademy.orderpaymentrefund.service.paymentsupplier.paymentstrategyimpl;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.paymentsupplier.PaymentStrategy;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component("point")
public class PointPayment implements PaymentStrategy {

    @Override
    public PaymentsResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {
        return PaymentsResponseDto.builder()
            .paymentKey(approvePaymentRequestDto.getPaymentKey())
            .totalAmount(approvePaymentRequestDto.getAmount())
            .method("포인트 결제")
            .orderId(approvePaymentRequestDto.getOrderId())
            .build();
    }

    @Override
    public void paymentRefund(long orderId, String cancelReason) {

    }
}
