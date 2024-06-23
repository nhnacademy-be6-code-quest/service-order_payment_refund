package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @NotNull
    private long quantity;

    @NotNull
    @Column(name = "price_per_product")
    private long productPrice;

    @NotNull
    private long productId; // fk

    @Builder
    public OrderDetail(Order order, long quantity, long productPrice, long productId){
        this.order = order;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.productId = productId;
    }

}
