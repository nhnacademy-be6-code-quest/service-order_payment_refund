package com.nhnacademy.orderpaymentrefund.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ProductOrderDetailOption 엔티티 : 주문한 상품에 선택된 옵션 상품 엔티티
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductOrderDetailOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOrderDetailOptionId;

    @NotNull
    private Long productId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_order_detail_id")
    private ProductOrderDetail productOrderDetail;

    @NotNull
    @Size(max = 255)
    private String optionProductName;

    @NotNull
    private Long optionProductPrice;

    @NotNull
    @Column(name = "option_product_quantity")
    private Long quantity;

    /**
     * 주문한 상품에 선택된 옵션 상품
     * @param productId 옵션상품 상품 아이디
     * @param productOrderDetail 연관된 주문상품.
     * @param optionProductName 옵션 상품 이름
     * @param optionProductPrice 옵션 상품 가격
     **/
    @Builder
    public ProductOrderDetailOption(long productId, ProductOrderDetail productOrderDetail, String optionProductName, long optionProductPrice, long quantity){
        this.productId = productId;
        this.productOrderDetail = productOrderDetail;
        this.optionProductName = optionProductName;
        this.optionProductPrice = optionProductPrice;
        this.quantity = quantity;
    }
}
