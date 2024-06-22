package com.nhnacademy.orderpaymentrefund.repository.payment;

import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}
