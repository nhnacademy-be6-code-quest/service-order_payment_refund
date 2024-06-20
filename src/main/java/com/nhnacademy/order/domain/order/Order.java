package com.nhnacademy.order.domain.order;

//import com.nhnacademy.order.domain.shipping.ShippingPolicy;
import com.nhnacademy.order.domain.shipping.ShippingPolicy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;
@Table(name = "\"ORDER\"")
@Entity
@NoArgsConstructor
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @NotNull
    private ZonedDateTime orderDate;

    @NotNull
    private ZonedDateTime deliveryDate;

    //@ColumnDefault("결제대기")
    private String orderStatus;

    @NotNull
    private long totalPrice; // 지불 금액

    @NotNull
    private long clientId;

}