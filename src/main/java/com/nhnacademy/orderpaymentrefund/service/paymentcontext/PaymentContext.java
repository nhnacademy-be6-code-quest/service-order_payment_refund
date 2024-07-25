package com.nhnacademy.orderpaymentrefund.service.paymentcontext;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.paymentsupplier.PaymentStrategy;
import com.nhnacademy.orderpaymentrefund.service.paymentsupplier.PaymentSupplier;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentContext {

    private final PaymentSupplier paymentSupplier;

    public PaymentsResponseDto paymentApprove(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {
        PaymentStrategy paymentStrategy = paymentSupplier.getSupplier(
            approvePaymentRequestDto.getMethodType().toLowerCase());

        return paymentStrategy.approvePayment(approvePaymentRequestDto);
    }

    public void paymentRefund(String methodType, long orderId, String cancelReason) {
        PaymentStrategy paymentStrategy = paymentSupplier.getSupplier(methodType.toLowerCase());
        paymentStrategy.paymentRefund(orderId, cancelReason);
    }


}
