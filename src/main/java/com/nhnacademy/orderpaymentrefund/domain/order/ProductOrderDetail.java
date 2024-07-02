package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ProductOrderDetail 엔티티 : 주문 상세 엔티티
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prduct_order_detail_id")
    private long productOrderDetailId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    private long productId; // fk

    @NotNull
    private long quantity; // 상품 구매 수량

    @NotNull
    private long pricePerProduct; // 상품 개당 가격

    /**
     * 주문 상세 빌더
     * @param order 연관된 주문. 주문 상품에 대한 주문 데이터.
     * @param productId 상품 아이디
     * @param quantity 상품 수량
     * @param pricePerProduct 개당 상품`
     **/
    @Builder
    public ProductOrderDetail(Order order, long productId, long quantity, long pricePerProduct) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerProduct = pricePerProduct;
    }

}
