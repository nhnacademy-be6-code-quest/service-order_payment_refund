package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossApprovePaymentRequest;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.UserUpdateGradeRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public TossPaymentsResponseDto approvePayment(@RequestBody TossApprovePaymentRequest tossApprovePaymentRequest) throws ParseException {
        return paymentService.approvePayment(tossApprovePaymentRequest);
    }

    @Override
    public void savePayment(@RequestBody TossPaymentsResponseDto tossPaymentsResponseDto, @RequestHeader HttpHeaders headers) {
        paymentService.savePayment(headers, tossPaymentsResponseDto);
    }

    @Override
    public PaymentGradeResponseDto getPaymentRecordOfClient(@PathVariable Long clientId) {
        return paymentService.getPaymentRecordOfClient(clientId);
    }

    @Override
    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(@RequestHeader HttpHeaders headers, @RequestParam("orderCode") String orderCode){
        return paymentService.getPostProcessRequiredPaymentResponseDto(headers, orderCode);
    }


}