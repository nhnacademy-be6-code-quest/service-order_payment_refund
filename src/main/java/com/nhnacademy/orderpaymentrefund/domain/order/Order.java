package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private LocalDateTime deliveryDate; // 배송날짜

    private OrderStatus orderStatus; // 상태

    @NotNull
    private long totalPrice; // 지불 예상 금액

    @NotNull
    private long clientId; // fk

    @Column(name = "shipping_fee_of_order_date")
    private long shippingFee; // 주문당시 배송비

    @Column(name = "phone_number")
    private String phoneNumber; // 주문당시 핸드폰 번호

    @Column(name = "client_delivery_address")
    private String deliveryAddress; // 받는 주소

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @Builder
    public Order(LocalDateTime deliveryDate, long totalPrice, long clientId, long shippingFee, String phoneNumber, String deliveryAddress){
        this.orderDate = LocalDateTime.now();
        this.deliveryDate = deliveryDate == null ? orderDate.plusDays(1) : deliveryDate;
        this.orderStatus = OrderStatus.WAIT_PAYMENT;
        this.totalPrice = totalPrice;
        this.clientId = clientId;
        this.shippingFee = shippingFee;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
    }

    public void updateOrder(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

}
