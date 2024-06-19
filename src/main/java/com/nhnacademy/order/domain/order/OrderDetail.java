package com.nhnacademy.order.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderDetailId;

    @ManyToOne(optional = false)
    private Order order;

    @NotNull
    private long quantity;

    @NotNull
    private long pricePerProduct;

    @NotNull
    private long product_id;

}
