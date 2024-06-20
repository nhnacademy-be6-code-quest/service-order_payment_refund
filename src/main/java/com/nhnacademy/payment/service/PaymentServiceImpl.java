package com.nhnacademy.payment.service;

import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.dto.PaymentCreateRequestGet;
import com.nhnacademy.payment.dto.PaymentResponse;
import com.nhnacademy.payment.exception.PaymentNotFoundException;
import com.nhnacademy.payment.repository.PaymentRepository;
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

    private final PaymentRepository paymentRepository;

    @Autowired
    PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse findPaymentByOrderId(Long orderId) {
        return null;
    }

    @Override
    public PaymentResponse findPaymentByPaymentId(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
            PaymentNotFoundException::new);
        return PaymentResponse.from(payment);
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void tryPayment(PaymentCreateRequestGet paymentCreateRequestGet) {

    }
}
