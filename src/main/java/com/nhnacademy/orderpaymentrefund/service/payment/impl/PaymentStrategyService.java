package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStrategyService {

    private final PGServiceStrategyProvider PGServiceStrategyProvider;

    public PaymentViewRequestDto getPaymentViewRequestDto(String pgName, String orderCode) {
        PGServiceStrategy pgServiceStrategy = PGServiceStrategyProvider.getPaymentStrategy(pgName);
        return pgServiceStrategy.getPaymentViewRequestDto(orderCode);
    }

    public PaymentsResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {

        PGServiceStrategy PGServiceStrategy = PGServiceStrategyProvider.getPaymentStrategy(
            approvePaymentRequestDto.getMethodType().toLowerCase());

        return PGServiceStrategy.approvePayment(approvePaymentRequestDto);


    }

    public void refundPayment(String paymentType, long orderId, String cancelReason){

        PGServiceStrategy PGServiceStrategy = PGServiceStrategyProvider.getPaymentStrategy(paymentType.toLowerCase());

        PGServiceStrategy.refundPayment(orderId, cancelReason);

    }

}
