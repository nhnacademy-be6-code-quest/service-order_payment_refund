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
public class RefundPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refundPolicyId;

    @NotNull
    private String refundPolicyReason;

    @NotNull
    private String refundPolicyType;

    @NotNull
    private int refundShippingFee;

    @NotNull
    private Boolean refundPolicyStatus;
}