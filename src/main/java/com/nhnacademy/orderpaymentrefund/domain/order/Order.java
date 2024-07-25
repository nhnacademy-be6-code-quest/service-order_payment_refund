package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Order 엔티티
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @NotNull
    @Column(unique = true)
    private String orderCode; // 결제에 필요한 uuid

    @NotNull
    private LocalDateTime orderDatetime; // 주문 일시

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private Long productTotalAmount; // 상품 총 금액

    @NotNull
    @Column(name = "shipping_fee_of_order_date")
    private Integer shippingFee; // 배송비

    @NotNull
    private Long orderTotalAmount; // 주문 총 금액 = 상품 총 금액 + 배송비

    @Nullable
    @Column(name = "designated_delivery_date")
    private LocalDate designatedDeliveryDate; // 지정 배송날짜

    @Nullable
    private LocalDate deliveryStartDate; // 출고일

    @NotNull
    @Pattern(regexp = "^(010|011)\\d{8}$", message = "전화번호는 010 또는 011로 시작하는 11자리 숫자여야 합니다.")
    private String phoneNumber;

    @NotNull
    @Size(max = 512)
    private String deliveryAddress; // 배송 주소지

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<ProductOrderDetail> productOrderDetailList;

    /**
     * Order 엔티티 생성 빌더
     *
     * @param productTotalAmount 상품 총 금액. 주문한 상품들의 총 금액. (옵션 상품 포함)
     * @param designatedDeliveryDate 지정 날짜. 주문자가 지정하지 않으면 null.
     * @param phoneNumber 주문자 핸드폰 번호
     * @param deliveryAddress 배송지 주소
     **/

    @SuppressWarnings("java:S107") // be sure this construct
    @Builder
    public Order(@NotNull String orderCode, @NotNull Long productTotalAmount, @NotNull Integer shippingFee, @Nullable LocalDate designatedDeliveryDate,
                 @NotNull String phoneNumber, @NotNull String deliveryAddress){
        this.orderCode = orderCode;
        this.productTotalAmount = productTotalAmount;
        this.shippingFee = shippingFee;
        this.designatedDeliveryDate = designatedDeliveryDate;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.orderDatetime = LocalDateTime.now();
        this.orderStatus = OrderStatus.PAYED;
        this.orderTotalAmount = this.productTotalAmount + this.shippingFee;
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
