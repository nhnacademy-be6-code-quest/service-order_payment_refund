package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Nullable
    @Column(unique = true)
    private Long clientId; // fk

    @Nullable
    private Long couponId; // fk

    @NotNull
    private long pointPolicyId; // fk

    @NotNull
    @Column(unique = true)
    private UUID tossOrderId;

    @NotNull
    private LocalDateTime orderDate; // 주문 일시

    @Size(max = 50)
    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private long productTotalAmount; // 상품 총 금액

    @NotNull
    @Column(name = "shipping_fee_of_order_date")
    private int shippingFee; // 배송비

    @NotNull
    private long orderTotalAmount; // 주문 총 금액 = 상품 총 금액 + 배송비

    @Nullable
    @Column(name = "designated_delivery_date")
    private LocalDate designatedDeliveryDate; // 지정 배송날짜

    @Nullable
    private LocalDate deliveryStartDate; // 출고일

    @NotNull
    @Pattern(regexp = "/^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$/")
    private String phoneNumber;

    @NotNull
    @Size(max = 512)
    @Column(name = "client_delivery_address")
    private String deliveryAddress; // 배송 주소지

    @NotNull
    private long discountAmountByCoupon; // 쿠폰 할인금액

    @NotNull
    private long discountAmountByPoint; // 포인트 할인금액

    @NotNull
    private long accumulatedPoint; // 적립 포인트

    @Size(max = 255)
    @Nullable
    private String nonClientOrderPassword; // 비회원 주문 비밀번호

    @Size(max = 128)
    @Nullable
    private String nonClientOrdererName; // 비회원 주문자 이름

    @Size(max = 320)
    @Nullable
    private String nonClientOrdererEmail; // 비회원 주문자 이메일

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<ProductOrderDetail> productOrderDetailList;

    /**
     * 회원을 위한 Order 엔티티 생성 빌더
     *
     * @param clientId 회원 아이디
     * @param couponId 쿠폰 아이디. 쿠폰을 사용하지 않으면 null. 쿠폰 사용 시 not null
     * @param pointPolicyId 쿠폰 정책 아이디. 쿠폰을 사용하지 않으면 null.
     * @param tossOrderId 토스 페이먼츠 아이디(uuid). PG service (토스페이먼츠)에 넘길 주문 고유값.
     * @param productTotalAmount 상품 총 금액. 주문한 상품들의 총 금액.
     * @param shippingFee 배송비. 주문당시 배송정책에 따른 배송비.
     * @param designatedDeliveryDate 지정 날짜. 주문자가 지정하지 않으면 null.
     * @param phoneNumber 주문자 핸드폰 번호
     * @param deliveryAddress 배송지 주소
     * @param discountAmountByCoupon 쿠폰 할인 금액. 쿠폰을 사용하지 않으면 0.
     * @param discountAmountByPoint 포인트 할인 금액. 포인트를 사용하지 않으면 0.
     * @param accumulatedPoint 적립 포인트.
     *
     **/
    @Builder(builderMethodName = "clientOrderBuilder", builderClassName = "clientOrderBuilder")
    public Order(long clientId, @Nullable Long couponId, long pointPolicyId, UUID tossOrderId, long productTotalAmount, int shippingFee, @Nullable LocalDate designatedDeliveryDate,
                 String phoneNumber, String deliveryAddress, long discountAmountByCoupon, long discountAmountByPoint, long accumulatedPoint){
        this.clientId = clientId;
        this.couponId = couponId;
        this.pointPolicyId = pointPolicyId;
        this.tossOrderId = tossOrderId;
        this.productTotalAmount = productTotalAmount;
        this.shippingFee = shippingFee;
        this.designatedDeliveryDate = designatedDeliveryDate;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.discountAmountByCoupon = discountAmountByCoupon;
        this.discountAmountByPoint = discountAmountByPoint;
        this.accumulatedPoint = accumulatedPoint;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.WAIT_PAYMENT;
        this.orderTotalAmount = this.productTotalAmount + this.shippingFee;
    }

    /**
     * 비회원을 위한 Order 엔티티 생성 빌더
     *
     * @param tossOrderId 토스 페이먼츠 아이디(uuid). PG service (토스페이먼츠)에 넘길 주문 고유값.
     * @param productTotalAmount 상품 총 금액. 주문한 상품들의 총 금액.
     * @param shippingFee 배송비. 주문당시 배송정책에 따른 배송비.
     * @param designatedDeliveryDate 지정 날짜. 주문자가 지정하지 않으면 null.
     * @param phoneNumber 주문자 핸드폰 번호. 추후 조회용으로 사용됨.
     * @param deliveryAddress 배송지 주소
     * @param nonClientOrderPassword 비회원 주문 비밀번호. 추후 조회용으로 사용됨.
     * @param nonClientOrdererName 비회원 주문자 이름. 추후 조회용으로 사용됨.
     * @param nonClientOrdererEmail 비회원 주문자 이메일. 추후 조회용으로 사용됨.
     *
     **/
    @Builder(builderMethodName = "nonClientOrderBuilder", builderClassName = "nonClientOrderBuilder")
    public Order(UUID tossOrderId, long productTotalAmount, int shippingFee, @Nullable LocalDate designatedDeliveryDate, String phoneNumber,
                 String deliveryAddress, @Nullable String nonClientOrderPassword, @Nullable String nonClientOrdererName, @Nullable String nonClientOrdererEmail){
        this.tossOrderId = tossOrderId;
        this.productTotalAmount = productTotalAmount;
        this.shippingFee = shippingFee;
        this.designatedDeliveryDate = designatedDeliveryDate;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.WAIT_PAYMENT;
        this.orderTotalAmount = this.productTotalAmount + this.shippingFee;
        this.deliveryAddress = deliveryAddress;
        this.nonClientOrderPassword = nonClientOrderPassword;
        this.nonClientOrdererName = nonClientOrdererName;
        this.nonClientOrdererEmail = nonClientOrdererEmail;
    }

    /**
     * Order의 orderStatus(주문 상태) 필드를 업데이트하기 위한 메서드
     * @param orderStatus 변경할 주문 상태
     **/
    public void updateOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    /**
     * Order의 deliveryStartDate(출고일) 필드를 설정하기 위한 메서드
     **/
    public void updateDeliveryStartDate(){this.deliveryStartDate = LocalDate.now();}

}
