package com.nhnacademy.payment.repository;

import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.dto.PaymentRequestDto;
import com.nhnacademy.payment.dto.PaymentResponseDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}