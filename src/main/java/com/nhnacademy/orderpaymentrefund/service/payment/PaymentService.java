package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.OrderPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface PaymentService {
    void savePayment(long orderId, PaymentResponseDto paymentResponseDto);
    PaymentResponseDto findByPaymentId(Long paymentId);
    OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(@PathVariable Long orderId);
}
