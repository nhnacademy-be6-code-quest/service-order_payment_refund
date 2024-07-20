package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostProcessRequiredPaymentResponseDto { // 후처리에 필요한 데이터.
    private Long clientId;
    private long amount; // 결제금액
    private String paymentMethodName;
    private List<Long> productIdList; // 장바구니 중 구매한 상품 아이디 리스트.

    public void addProductIdList(Long productId) {
        if(productIdList == null) {
            productIdList = new ArrayList<>();
        }
        productIdList.add(productId);
    }

    @Builder
    public PostProcessRequiredPaymentResponseDto(Long clientId, long amount, String paymentMethodName){
        this.clientId = clientId;
        this.amount = amount;
        this.paymentMethodName = paymentMethodName;
        this.productIdList = new ArrayList<>();
    }

}
