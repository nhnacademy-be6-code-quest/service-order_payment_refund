package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.OrderPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface PaymentService {
    void savePayment(PaymentRequestDto paymentRequestDto, long orderId);
    PaymentResponseDto findByPaymentId(Long paymentId);
    OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(@PathVariable Long orderId);
}
