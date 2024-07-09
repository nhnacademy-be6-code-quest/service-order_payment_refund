package com.nhnacademy.orderpaymentrefund.repository.refund;

import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundAndCancelPolicy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundPolicyRepository extends JpaRepository<RefundAndCancelPolicy, Long> {

    List<RefundAndCancelPolicy> findAll();
}