package com.nhnacademy.orderpaymentrefund.domain.shipping;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class ShippingPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_policy_id")
    private long id;

    @ColumnDefault("")
    @Column(name = "shipping_policy_description")
    private String description;

    @NotNull
    @Column(name = "shipping_fee")
    private int fee;

    @NotNull
    @Column(name = "min_purchase_amount")
    private int lowerBound; // 최소 주문 금액
}
