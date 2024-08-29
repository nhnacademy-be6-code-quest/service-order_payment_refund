package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderForm;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.impl.NaverPaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(value = "naver")
@RequiredArgsConstructor
public class NaverPGServiceStrategy implements PGServiceStrategy {

    private final PGServiceUtil pgServiceUtil;

    @Override
    public PaymentViewRequestDto getPaymentViewRequestDto(String orderCode) {
        OrderForm orderForm = pgServiceUtil.getOrderForm(orderCode);
        String productName = pgServiceUtil.getOrderHistoryTitle(orderForm);
        long amount = orderForm.getTotalPayAmount();

        return NaverPaymentViewRequestDto.builder()
                .productName(productName)
                .orderCode(orderCode)
                .amount(amount)
                .taxScopeAmount(amount)
                .taxExScopeAmount(0L)
                .build();
    }

    @Override
    public PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) {
        return null;
    }

    @Override
    public void refundPayment(long orderId, String cancelReason) {

    }

}
