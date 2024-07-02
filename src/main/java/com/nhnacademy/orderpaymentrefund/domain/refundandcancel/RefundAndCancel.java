package com.nhnacademy.orderpaymentrefund.domain.refundandcancel;

import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RefundAndCancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refundAndCancelId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "refund_and_cancel_policy_id")
    private RefundAndCancelPolicy refundAndCancelPolicy;

    @NotNull
    private LocalDateTime refundAndCancelDatetime;

    private String refundAndCancelDetailReason;
}