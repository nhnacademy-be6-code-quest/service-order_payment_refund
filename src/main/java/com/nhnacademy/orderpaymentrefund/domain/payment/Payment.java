package com.nhnacademy.orderpaymentrefund.domain.payment;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Virtus_Chae
 * @version 1.0
 */

@Entity
@NoArgsConstructor
@Getter
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

    @JoinColumn(name = "payment_method_type_id")
    @ManyToOne
    private PaymentMethodType paymentMethodType;

    @NotNull
    private LocalDateTime payTime;

    @NotNull
    private String paymentMethodName;

    @NotNull
    private String paymentKey;

    @Builder
    public Payment(Order order, long payAmount, String paymentMethodName,PaymentMethodType paymentMethodType,
        String paymentKey) {
        this.order = order;
        this.paymentMethodType=paymentMethodType;
        this.payAmount = payAmount;
        this.payTime = LocalDateTime.now();
        this.paymentMethodName = paymentMethodName;
        this.paymentKey = paymentKey;
    }
}