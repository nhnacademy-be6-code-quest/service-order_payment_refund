package com.nhnacademy.orderpaymentrefund.domain.refundandcancel;

import com.nhnacademy.orderpaymentrefund.domain.RefundPolicyStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
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
    private LocalDateTime refundPolicyExpirationDate;
    private RefundPolicyStatus refundPolicyStatus;

    public void setRefundPolicyOff (RefundPolicyStatus refundPolicyStatus){
        this.refundPolicyExpirationDate=LocalDateTime.now();
        this.refundPolicyStatus = refundPolicyStatus;
    }

    @Builder
    public RefundPolicy (String refundPolicyType, int refundShippingFee, LocalDateTime refundPolicyIssuedDate, RefundPolicyStatus refundPolicyStatus) {
        this.refundPolicyType = refundPolicyType;
        this.refundShippingFee = refundShippingFee;
        this.refundPolicyIssuedDate = refundPolicyIssuedDate;
        this.refundPolicyStatus = refundPolicyStatus;
    }
}