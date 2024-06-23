package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController
@RequestMapping("/order/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentServiceImpl paymentServiceImpl;

    @PostMapping
    public void savePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        log.info(paymentRequestDto.toString());
        paymentServiceImpl.savePayment(paymentRequestDto); // 여기에서 에러 발생
    }

    @GetMapping("{paymentId}")
    public PaymentResponseDto findPaymentByPaymentId(@RequestParam Long paymentId) {
        return paymentServiceImpl.findPaymentByPaymentId(paymentId);
    }
}