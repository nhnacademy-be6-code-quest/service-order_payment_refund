package com.nhnacademy.orderpaymentrefund.domain.payment;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author Virtus_Chae
 * @version 1.0
 */

@Entity
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "order_id"/*, unique = true*/) // Unique 처리에 대해 생각해 보기
    private Order order;

    @NotNull
    private long payAmount;

    @NotNull
    private LocalDateTime payTime;

    @NotNull
    private String paymentMethodName;

    @NotNull
    private String tossPaymentKey;

    @Builder
    public Payment(Order order, long payAmount, LocalDateTime payTime, String paymentMethodName,
        String tossPaymentKey) {
        this.order = order;
        this.payAmount = payAmount;
        this.payTime = payTime;
        this.paymentMethodName = paymentMethodName;
        this.tossPaymentKey = tossPaymentKey;
    }
}