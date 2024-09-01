package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentSaveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface PaymentService {
    Payment savePayment(Order order, PaymentSaveRequestDto paymentSaveRequestDto);
    PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId);
    PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(HttpHeaders headers, String orderCode);
    List<PaymentMethodResponseDto> getPaymentMethodList();
    PaymentApproveResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto);
}
