package com.nhnacademy.orderpaymentrefund.domain.refundandcancel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RefundAndCancelPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refundAndCancelPolicyId;

    @NotNull
    private String refundAndCancelPolicyReason;

    @NotNull
    private String refundAndCancelPolicyType;

    private int refundShippingFee;
}