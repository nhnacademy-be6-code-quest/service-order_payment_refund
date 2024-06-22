package com.nhnacademy.payment.service;

import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.domain.PaymentMethod;
import com.nhnacademy.payment.dto.PaymentRequestDto;
import com.nhnacademy.payment.dto.PaymentResponseDto;
import com.nhnacademy.payment.repository.PaymentMethodRepository;
import com.nhnacademy.payment.repository.PaymentRepository;
import com.nhnacademy.temp.OrderRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;

    // Order Enum Type -> String, 배송 상태 -> tinyInt
    public void savePayment(PaymentRequestDto paymentRequestDto) {
        Order order = orderRepository.findById(paymentRequestDto.orderId()).orElse(null);


        // 여기에서 이상이 있는 듯 -> Payment Method 가 없어서 문제가 생긴 것으로 보임
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequestDto.paymentMethodId()).orElse(null);
        Payment payment = Payment.builder()
            .order(order)
            .paymentMethod(paymentMethod)
            .couponId(paymentRequestDto.couponId())
            .payAmount(paymentRequestDto.payAmount())
            .payTime(LocalDateTime.now())
            .build();

        paymentRepository.save(payment);
    }

    public PaymentResponseDto findPaymentByPaymentId(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        return PaymentResponseDto.builder()
            .paymentId(payment.getPaymentId())
            .order(payment.getOrder())
            .paymentMethod(payment.getPaymentMethod())
            .couponId(payment.getCouponId())
            .payAmount(payment.getPayAmount())
            .payTime(payment.getPayTime())
            .build();
    }
}