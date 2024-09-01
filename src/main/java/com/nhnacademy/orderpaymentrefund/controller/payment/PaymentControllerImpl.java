package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 결제 정보를 저장하고, 조회하는 컨트롤러입니다. 결제 정보는 수정되거나 삭제되지 않습니다.
 *
 * @author 김채호
 * @version 1.0
 */

@RestController
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController{

    private final PaymentService paymentService;

    @Override
    public PaymentApproveResponseDto approvePayment(@RequestBody ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException {
        return paymentService.approvePayment(approvePaymentRequestDto);
    }

    @Override
    public PaymentGradeResponseDto getPaymentRecordOfClient(@PathVariable Long clientId) {
        return paymentService.getPaymentRecordOfClient(clientId);
    }

    @Override
    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(@RequestHeader HttpHeaders headers, @RequestParam("orderCode") String orderCode){
        return paymentService.getPostProcessRequiredPaymentResponseDto(headers, orderCode);
    }

    @Override
    public List<PaymentMethodResponseDto> getAllPaymentMethod() {
        return paymentService.getPaymentMethodList();
    }

}