package com.nhnacademy.orderpaymentrefund.repository.payment;

import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodTypeRepository extends JpaRepository<PaymentMethodType, Long> {

    PaymentMethodType findByPaymentMethodTypeNameEquals(String paymentMethodTypeName);

}
