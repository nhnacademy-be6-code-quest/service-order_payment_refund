package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossApprovePaymentRequest;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 결제 정보를 저장하고, 조회하는 컨트롤러입니다. 결제 정보는 수정되거나 삭제되지 않습니다.
 *
 * @author 김채호
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/api/order/payment/approve")
    public TossPaymentsResponseDto approvePayment(@RequestBody TossApprovePaymentRequest tossApprovePaymentRequest) throws ParseException {
        return paymentService.approvePayment(tossApprovePaymentRequest);
    }

    @PostMapping("/api/order/payment/save")
    public void savePayment(@RequestBody TossPaymentsResponseDto tossPaymentsResponseDto, @RequestHeader HttpHeaders headers, HttpServletResponse response) {
        paymentService.savePayment(headers, tossPaymentsResponseDto, response);
    }

    @GetMapping("/api/payment/grade/{clientId}")
    public PaymentGradeResponseDto getPaymentRecordOfClient(@PathVariable Long clientId) {
        return paymentService.getPaymentRecordOfClient(clientId);
    }

    @GetMapping("/api/order/payment/post-process")
    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(@RequestParam("tossOrderId") String tossOrderId){
        return paymentService.getPostProcessRequiredPaymentResponseDto(tossOrderId);
    }

}