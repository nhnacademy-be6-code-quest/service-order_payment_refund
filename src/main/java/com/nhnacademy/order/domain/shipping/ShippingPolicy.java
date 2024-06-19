package com.nhnacademy.order.domain.shipping;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class ShippingPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shippingPolicyId;

    @ColumnDefault("")
    private String shippingPolicyDescription;

    @NotNull
    private int shippingFee;

    @NotNull
    private int minPurchaseAmount;
}
