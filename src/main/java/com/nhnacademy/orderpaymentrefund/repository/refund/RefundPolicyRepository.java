package com.nhnacademy.orderpaymentrefund.repository.refund;

import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundPolicyRepository extends JpaRepository<RefundPolicy, Long> {

}