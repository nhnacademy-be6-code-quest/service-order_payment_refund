package com.nhnacademy.payment.controller;

import com.nhnacademy.payment.dto.PaymentRequestDto;
import com.nhnacademy.payment.dto.PaymentResponseDto;
import com.nhnacademy.payment.service.PaymentService;
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
@RequestMapping("order/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public void savePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        log.info(paymentRequestDto.toString());
        paymentService.savePayment(paymentRequestDto); // 여기에서 에러 발생
    }

    @GetMapping("{paymentId}")
    public PaymentResponseDto findPaymentByPaymentId(@RequestParam Long paymentId) {
        return paymentService.findPaymentByPaymentId(paymentId);
    }
}