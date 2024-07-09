package com.nhnacademy.orderpaymentrefund.repository.refund;

import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundAndCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundAndCancel, Long> {

}