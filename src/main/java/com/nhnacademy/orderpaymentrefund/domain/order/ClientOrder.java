package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_order_id")
    private Long clientOrderId;

    @NotNull
    private Long clientId; // fk

    private Long couponId; // fk

    @NotNull
    private Long discountAmountByCoupon; // 쿠폰 할인금액

    @NotNull
    private Long discountAmountByPoint; // 포인트 할인금액

    @NotNull
    private Long accumulatedPoint; // 적립 포인트

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public ClientOrder(Long clientId, Long couponId, Long discountAmountByCoupon,
        Long discountAmountByPoint, Long accumulatedPoint, Order order) {
        this.clientId = clientId;
        this.couponId = couponId;
        this.discountAmountByCoupon = discountAmountByCoupon;
        this.discountAmountByPoint = discountAmountByPoint;
        this.accumulatedPoint = accumulatedPoint;
        this.order = order;
    }

}
