package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;

public interface PaymentService {
    void savePayment(HttpHeaders headers, PaymentsResponseDto paymentsResponseDto);
    PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId);
    PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(HttpHeaders headers, String orderCode);
}
