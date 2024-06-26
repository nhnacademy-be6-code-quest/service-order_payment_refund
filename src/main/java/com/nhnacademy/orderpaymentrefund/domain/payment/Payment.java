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
@Getter
@Entity
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    private Long couponId;

    @NotNull
    private Long payAmount;

    @NotNull
    private LocalDateTime payTime;

    @Builder
    public Payment(Order order, PaymentMethod paymentMethod, Long couponId, Long payAmount) { // Payment.builder().order(new Order()).paymentMethod(new PaymentMethod())...
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.couponId = couponId;
        this.payAmount = payAmount;
        this.payTime = LocalDateTime.now();
    }
}