package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NonClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "non_client_order_id")
    private Long nonClientOrderId;

    @Size(max = 255)
    @Nullable
    private String nonClientOrderPassword; // 비회원 주문 비밀번호

    @Size(max = 128)
    @Nullable
    private String nonClientOrdererName; // 비회원 주문자 이름

    @Size(max = 320)
    @Nullable
    private String nonClientOrdererEmail; // 비회원 주문자 이메일

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public NonClientOrder(@Nullable String nonClientOrderPassword,
        @Nullable String nonClientOrdererName, @Nullable String nonClientOrdererEmail,
        Order order) {
        this.nonClientOrderPassword = nonClientOrderPassword;
        this.nonClientOrdererName = nonClientOrdererName;
        this.nonClientOrdererEmail = nonClientOrdererEmail;
        this.order = order;
    }
}
