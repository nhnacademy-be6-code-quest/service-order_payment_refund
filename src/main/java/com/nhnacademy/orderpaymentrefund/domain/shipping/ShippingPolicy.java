package com.nhnacademy.orderpaymentrefund.domain.shipping;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_policy_id")
    private long id;

    @Column(name = "shipping_policy_description")
    private String description;

    @NotNull
    @Column(name = "shipping_fee")
    private int fee;

    @NotNull
    @Column(name = "min_purchase_amount")
    private int lowerBound; // 최소 주문 금액

    public void updatePost(String description, int fee, int lowerBound){
        this.description = description;
        this.fee = fee;
        this.lowerBound = lowerBound;
    }
}
