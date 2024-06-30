package com.nhnacademy.orderpaymentrefund.domain.shipping;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * 배송지 정책 엔티티
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_policy_id")
    private long id;

    @Column(name = "shipping_policy_description")
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    @Column(name = "shipping_fee")
    private int shippingFee;

    @NotNull
    @Column(name = "min_purchase_amount")
    private int minPurchaseAmount; // 최소 주문 금액

    @NotNull
    private ShippingPolicyType shippingPolicyType;

    public void updateShippingPolicy(String description, int shippingFee, int minPurchaseAmount){
        this.description = description;
        this.shippingFee = shippingFee;
        this.minPurchaseAmount = minPurchaseAmount;
    }
}
