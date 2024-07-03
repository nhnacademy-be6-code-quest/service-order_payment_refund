package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/api/client/order/{orderId}/payment")
    public void savePayment(@PathVariable long orderId,
        @RequestBody PaymentResponseDto paymentResponseDto) {
        paymentService.savePayment(orderId, paymentResponseDto);
    }

    @GetMapping("/api/client/order/payment/{paymentId}")
    public PaymentResponseDto findByPaymentId(@PathVariable Long paymentId) {
        return paymentService.findByPaymentId(paymentId);
    }
//    TODO: 추후 구현해야 함.
//    @GetMapping("/api/client/order/{orderId}/payment")
//    public long findTotalPriceByOrderId(@PathVariable Long orderId) {
//        return orderService.getTotalPrice(orderId);
//    }
//
//    @GetMapping("/api/client/order/{orderId}/payment")
//    OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(@PathVariable Long orderId) {
//        return paymentService.findOrderPaymentResponseDtoByOrderId(orderId);
//    }
}