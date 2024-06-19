package com.nhnacademy.order.domain.order;

import com.nhnacademy.order.domain.shipping.ShippingPolicy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @NotNull
    private ZonedDateTime orderDate;

    @NotNull
    private ZonedDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @NotNull
    private long totalPrice; // 지불 금액

    @NotNull
    private long clientId; // fk

    @NotNull
    @OneToOne
    @JoinColumn(name = "shipping_policy_id", referencedColumnName = "shipping_policy_id")
    private ShippingPolicy shippingPolicy;

    @Column(name = "shipping_fee_of_order_date")
    private long shippingFee; // 구매당시 배송비

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @Builder
    public Order(ZonedDateTime orderDate, ZonedDateTime deliveryDate, OrderStatus orderStatus, long totalPrice, long clientId, ShippingPolicy shippingPolicy, long shippingFee){
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.clientId = clientId;
        this.shippingPolicy = shippingPolicy;
        this.shippingFee = shippingFee;
    }

}
