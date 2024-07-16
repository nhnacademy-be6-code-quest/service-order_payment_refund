package com.nhnacademy.orderpaymentrefund.domain.refundandcancel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String refundPolicyType;

    @NotNull
    private int refundShippingFee;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    private LocalDateTime refundPolicyIssuedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    @Setter
    private LocalDateTime refundPolicyExpirationDate;

    public RefundPolicy(String refundPolicyType, int refundShippingFee) {
        this.refundPolicyType = refundPolicyType;
        this.refundShippingFee = refundShippingFee;
        this.refundPolicyIssuedDate = LocalDateTime.now();
    }
}