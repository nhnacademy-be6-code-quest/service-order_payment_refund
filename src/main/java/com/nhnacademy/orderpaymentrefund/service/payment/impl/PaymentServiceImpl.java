package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.OrderPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
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
    private final OrderRepository orderRepository;

    // Order Enum Type -> String, 배송 상태 -> tinyInt
    @Override
    public void savePayment(long orderId, PaymentResponseDto paymentResponseDto) {

        Payment payment = Payment.builder()
            .order(
                orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException()))
            .payAmount(paymentResponseDto.getPayAmount())
            .payTime(LocalDateTime.now())
            .paymentMethodName("CARD")
            .tossPaymentKey(paymentResponseDto.getTossPaymentKey())
            .build();
        paymentRepository.save(payment);
    }

    @Override
    public PaymentResponseDto findByPaymentId(Long paymentId) {
        return null;
    }

    @Override
    public OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(Long orderId) {
        return null;
    }
}