package com.nhnacademy.orderpaymentrefund.dto.product;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartCheckoutRequestDto {

    Long clientId;
    List<Long> productIdList;

    public void addProductId(Long productId) {
        if (productIdList == null) {
            productIdList = new ArrayList<>();
        }
        productIdList.add(productId);
    }

    @Builder
    public CartCheckoutRequestDto(Long clientId){
        this.clientId = clientId;
    }

}