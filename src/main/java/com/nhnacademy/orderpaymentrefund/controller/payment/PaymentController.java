package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/api/client/order/payment")
    public void savePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.savePayment(paymentRequestDto); // 여기에서 에러 발생
    }

    @GetMapping("/api/client/order/payment/{paymentId}")
    public PaymentResponseDto findByPaymentId(@PathVariable Long paymentId) {
        return paymentService.findByPaymentId(paymentId);
    }
}