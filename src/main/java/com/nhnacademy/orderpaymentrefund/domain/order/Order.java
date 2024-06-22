package com.nhnacademy.orderpaymentrefund.domain.order;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @NotNull
    private ZonedDateTime orderDate;

    @NotNull
    private ZonedDateTime deliveryDate;

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
    private long shippingFee; // 주문당시 배송비

    @Column(name = "client_delivery_address")
    private String phoneNumber; // 주문당시 핸드폰 번호

    @Column(name = "client_delivery_address")
    private String deliveryAddress; // 받는 주소

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @Builder
    public Order(ZonedDateTime orderDate, ZonedDateTime deliveryDate, OrderStatus orderStatus, long totalPrice, long clientId, ShippingPolicy shippingPolicy, long shippingFee, String phoneNumber, String deliveryAddress){
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.clientId = clientId;
        this.shippingPolicy = shippingPolicy;
        this.shippingFee = shippingFee;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
    }

}
