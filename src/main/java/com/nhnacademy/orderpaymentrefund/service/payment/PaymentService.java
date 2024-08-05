package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import java.util.List;
import org.springframework.http.HttpHeaders;

public interface PaymentService {
    void savePayment(HttpHeaders headers, PaymentsResponseDto paymentsResponseDto);
    PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId);
    PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(HttpHeaders headers, String orderCode);
    List<PaymentMethodResponseDto> getPaymentMethodList();
}
