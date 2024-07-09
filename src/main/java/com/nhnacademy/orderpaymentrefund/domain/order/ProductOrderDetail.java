package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @Column(name = "product_order_detail_id")
    private long productOrderDetailId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @NotNull
    private Long productId; // fk

    @NotNull
    private Long quantity; // 상품 구매 수량

    @NotNull
    private Long pricePerProduct; // 상품 개당 가격

    @NotNull
    private String productName; // 상품 이름

    @OneToMany(mappedBy = "productOrderDetail", fetch = FetchType.LAZY)
    private List<ProductOrderDetailOption> productOrderDetailOptionList;

    /**
     * 주문 상세 빌더
     * @param order 연관된 주문. 주문 상품에 대한 주문 데이터.
     * @param productId 상품 아이디
     * @param quantity 상품 수량
     * @param pricePerProduct 개당 상품
     * @param productName 상품 이름
     **/
    @Builder
    public ProductOrderDetail(Order order, @NotNull Long productId, @NotNull Long quantity, @NotNull Long pricePerProduct, @NotNull String productName) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerProduct = pricePerProduct;
        this.productName = productName;
    }

}
