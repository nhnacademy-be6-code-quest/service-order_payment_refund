package com.nhnacademy.payment.domain;

//import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.payment.dto.PaymentRequestDto;
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
    private LocalDateTime payTime;

//    @NotNull
//    @Column(name = "pay_amount")
//    private Long payAmount;

    @NotNull
    private Long clientDeliveryAddressId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PaymentMethodId")
    private PaymentMethod paymentMethod;

    private Long couponId;
}