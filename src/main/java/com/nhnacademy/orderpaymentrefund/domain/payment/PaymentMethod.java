package com.nhnacademy.orderpaymentrefund.domain.payment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Virtus_Chae
 * @version 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentMethodId;

    @NotNull
    private String paymentMethodName; // 토스 페이먼트가 제공하는 이름 - Enum 으로 해야 하는 이유가 있을까?

    @NotNull
    private boolean paymentMethodIsUsed; // 우리 서비스에서 지원하는지 여부
}