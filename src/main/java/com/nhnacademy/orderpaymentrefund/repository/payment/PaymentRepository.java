package com.nhnacademy.orderpaymentrefund.repository.payment;

import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}