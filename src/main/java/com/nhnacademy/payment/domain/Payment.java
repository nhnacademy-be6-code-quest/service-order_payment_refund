package com.nhnacademy.payment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Virtus_Chae
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자의 접근 지정자를 protected 로
@Builder // .builder() 메서드 자동 생성 -> builder 패던 자동으로 생성해 줌.
@Entity
//@Table(name = "a.payment") // 테이블 이름 매핑
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id") // 안 해도 되지만 일단 해 봤음
    private Long paymentId;

    @Setter
    @NotNull
    @Column(name = "order_id")
    private Long orderId;

    @Setter
    @NotNull
    @Column(name = "pay_time")
    private LocalDateTime payTime;

    @Setter
    @NotNull
    @Column(name = "client_delivery_address_id")
    private Long clientDeliveryAddressId;

    // ?
    @Setter
    @NotNull
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    // optional -> PaymentMethod 엔티티가 필수적으로 존재해야 함 / fetch -> 연관된 엔티티가 실제 사용할 때만 로드할 수 있도록 조치.
    // 단, fetch -> N+1 이슈 발생 가능. ToOne 의 Default 는 FetchType.EAGER(즉시 로딩)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Setter
    @NotNull
    @Column(name = "coupon_id")
    private Long couponId;

    // constructor? -> payment_id 없는 버전으로 하나 만들어 두어야
    public Payment (Long orderId, LocalDateTime payTime, Long clientDeliveryAddressId, PaymentMethod paymentMethod, Long couponId) {
        this.orderId = orderId;
        this.payTime = payTime;
        this.clientDeliveryAddressId = clientDeliveryAddressId;
        this.paymentMethod = paymentMethod;
        this.couponId = couponId;
    }
}
