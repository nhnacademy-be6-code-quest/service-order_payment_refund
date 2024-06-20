package com.nhnacademy.payment.service;

//import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.order.domain.order.OrderStatus;
import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.domain.PaymentMethod;
import com.nhnacademy.payment.dto.PaymentRequestDto;
import com.nhnacademy.payment.dto.PaymentResponseDto;
import com.nhnacademy.payment.repository.PaymentMethodRepository;
import com.nhnacademy.payment.repository.PaymentRepository;
import com.nhnacademy.temp.OrderRepository;
import java.lang.Thread.Builder;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequestDto.paymentMethodId()).orElse(null);
        Payment payment = Payment.builder()
            .order(order)
            .payTime(LocalDateTime.now())
            .clientDeliveryAddressId(paymentRequestDto.clientDeliveryAddressId())
            .paymentMethod(paymentMethod)
            .couponId(paymentRequestDto.couponId())
            .build();

        paymentRepository.save(payment);
    }

    public PaymentResponseDto findPaymentByPaymentId(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        return PaymentResponseDto.builder()
            .paymentId(payment.getPaymentId())
            .orderId(payment.getOrder().getOrderId())
            .payTime(payment.getPayTime())
            .clientDeliveryAddressId(payment.getClientDeliveryAddressId())
            .paymentMethod(payment.getPaymentMethod())
            .couponId(payment.getCouponId())
            .build();
    }
}