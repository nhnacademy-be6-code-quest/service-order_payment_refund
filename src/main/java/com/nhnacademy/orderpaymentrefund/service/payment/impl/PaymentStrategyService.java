package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStrategyService {

    private final PaymentStrategyGetter paymentStrategyGetter;

    public PaymentsResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {

        PaymentStrategy paymentStrategy = paymentStrategyGetter.getPaymentStrategy(
            approvePaymentRequestDto.getMethodType().toLowerCase());

        return paymentStrategy.approvePayment(approvePaymentRequestDto);


    }

    public void refundPayment(String paymentType, long orderId, String cancelReason){

        PaymentStrategy paymentStrategy = paymentStrategyGetter.getPaymentStrategy(paymentType.toLowerCase());

        paymentStrategy.refundPayment(orderId, cancelReason);

    }

}
