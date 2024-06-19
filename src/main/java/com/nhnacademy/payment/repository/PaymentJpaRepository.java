package com.nhnacademy.payment.repository;

import com.nhnacademy.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Virtus_Chae
 * @version 1.0
 */
public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

}