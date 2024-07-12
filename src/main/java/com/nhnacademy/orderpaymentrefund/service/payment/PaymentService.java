package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;

public interface PaymentService {

    void savePayment(long orderId, TossPaymentsResponseDto tossPaymentsResponseDto);
    PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId);
}
