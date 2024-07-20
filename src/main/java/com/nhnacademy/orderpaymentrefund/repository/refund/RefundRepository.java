package com.nhnacademy.orderpaymentrefund.repository.refund;


import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    Refund findByPayment(Payment payment);
}