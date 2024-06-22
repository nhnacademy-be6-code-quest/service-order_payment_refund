package com.nhnacademy.orderpaymentrefund.domain.payment;

//import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Virtus_Chae
 * @version 1.0
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
// 데이터에 박을 것
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PaymentMethodId")
    private PaymentMethod paymentMethod;

    private Long couponId;

    @NotNull
    private Long payAmount;

    @NotNull
    private LocalDateTime payTime;
}