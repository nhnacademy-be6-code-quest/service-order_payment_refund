package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentSaveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.ApprovePaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component(value = "point")
public class PointPGServiceStrategy implements PGServiceStrategy {
    @Override
    public PaymentViewRequestDto getPaymentViewRequestDto(String orderCode) {
        return null;
    }

    @Override
    public PaymentApproveResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException {
        return null;
    }

    @Override
    public SuccessPaymentOrderInfo getSuccessPaymentOrderInfo(PaymentApproveResponseDto approveResponseDto, Order order, Payment payment) {
        return null;
    }

    @Override
    public void refundPayment(long orderId, String cancelReason) {
        //not use
    }

    @Override
    public void setPaymentKey(String paymentKey) {

    }

    @Override
    public void setPaymentMethodName(String paymentMethodName) {

    }

}
