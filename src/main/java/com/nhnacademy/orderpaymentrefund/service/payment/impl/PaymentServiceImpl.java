package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethod;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentMethodRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;

    // Order Enum Type -> String, 배송 상태 -> tinyInt
    @Override
    public void savePayment(PaymentRequestDto paymentRequestDto) {
        Order order = orderRepository.findById(paymentRequestDto.getOrderId()).orElse(null);

        // 여기에서 이상이 있는 듯 -> Payment Method 가 없어서 문제가 생긴 것으로 보임
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequestDto.getPaymentMethodId()).orElse(null);
        Payment payment = Payment.builder()
            .order(order)
            .paymentMethod(paymentMethod)
            .couponId(paymentRequestDto.getCouponId())
            .payAmount(paymentRequestDto.getPayAmount())
            .payTime(LocalDateTime.now())
            .build();

        paymentRepository.save(payment);
    }

    @Override
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