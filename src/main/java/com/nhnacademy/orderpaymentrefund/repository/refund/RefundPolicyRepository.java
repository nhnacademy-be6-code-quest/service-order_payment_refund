package com.nhnacademy.orderpaymentrefund.repository.refund;

import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundPolicyRepository extends JpaRepository<RefundPolicy, Long> {
    RefundPolicy findByRefundPolicyType(String refundPolicyType);
    List<RefundPolicy> findByRefundPolicyExpirationDateIsNull();
    List<RefundPolicy> findByRefundPolicyExpirationDateIsNullAndRefundPolicyTypeNotContaining(String refundPolicyType);
}