package com.nhnacademy.payment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Virtus_Chae
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
//@Table(name = "a.payment_status") // 테이블 이름 매핑
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @NotNull
    @Setter
    @Column(name = "payment_method_name")
    private String paymentMethodName;

    public PaymentMethod (String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }
}