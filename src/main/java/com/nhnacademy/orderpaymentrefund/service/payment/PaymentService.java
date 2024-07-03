package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.OrderPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface PaymentService {
    void savePayment(long orderId, TossPaymentsResponseDto tossPaymentsResponseDto);
    TossPaymentsResponseDto findByPaymentId(Long paymentId);
    OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(@PathVariable Long orderId);
}
