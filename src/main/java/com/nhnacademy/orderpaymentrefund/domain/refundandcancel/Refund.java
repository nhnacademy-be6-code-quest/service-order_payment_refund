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
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김채호
 * @version 0.0
 */
@Getter
@Entity
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refundId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "refund_policy_id")
    private RefundPolicy refundPolicy;

    @NotNull
    private LocalDateTime refundDatetime;

    private String refundDetailReason;

    private Long refundAmount;

    public Refund(Payment payment, RefundPolicy refundPolicy, String refundDetailReason, Long refundAmount) {
        this.payment = payment;
        this.refundPolicy = refundPolicy;
        this.refundDatetime = LocalDateTime.now();
        this.refundDetailReason = refundDetailReason;
        this.refundAmount= refundAmount;
    }
}