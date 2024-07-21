package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossApprovePaymentRequest;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.UserUpdateGradeRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;

public interface PaymentService {
    void savePayment(HttpHeaders headers, TossPaymentsResponseDto tossPaymentsResponseDto);
    PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId);
    void updateUserGrade(UserUpdateGradeRequestDto userUpdateGradeRequestDto);
    TossPaymentsResponseDto approvePayment(TossApprovePaymentRequest tossApprovePaymentRequest) throws ParseException;
    PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(String tossOrderId);
}
