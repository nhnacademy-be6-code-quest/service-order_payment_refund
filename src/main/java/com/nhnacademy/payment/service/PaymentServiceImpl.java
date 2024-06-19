package com.nhnacademy.payment.service;

import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.dto.PaymentCreateRequestGet;
import com.nhnacademy.payment.dto.PaymentResponse;
import com.nhnacademy.payment.repository.PaymentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PaymentService 입니다. 미완성입니다.
 *
 * @author Virtus_Chae
 * @version 0.0
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentJpaRepository paymentJpaRepository;
//    private OrderRepository orderRepository;

    @Autowired
    PaymentServiceImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public PaymentResponse findPaymentByOrderId(Long orderId) {
//        orderRepository.findById(orderId);
        return null;
    }

    @Override
    public PaymentResponse findPaymentByPaymentId(Long paymentId) {
//        paymentJpaRepository.findById(paymentId);
        return null;
    }

    @Override
    public Payment savePayment(Payment payment) {
        return null;
    }

    @Override
    public void tryPayment(PaymentCreateRequestGet paymentCreateRequestGet) {

    }
}
