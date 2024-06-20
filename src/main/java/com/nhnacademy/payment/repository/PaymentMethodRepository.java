package com.nhnacademy.payment.repository;

import com.nhnacademy.payment.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}
