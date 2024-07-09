package com.nhnacademy.orderpaymentrefund.domain.refundandcancel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김채호
 * @version 0.0
 */
@Entity
@NoArgsConstructor
@Getter
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