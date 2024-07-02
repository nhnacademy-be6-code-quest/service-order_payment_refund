package com.nhnacademy.orderpaymentrefund.dto.order.field;

import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.util.List;

/**
 * OrderedProductAndOptionProductPairDto : 상품 주문했을 때, 해당 상품과 옵션상품 쌍
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record OrderedProductAndOptionProductPairDto (
        long productId,
        long productSinglePrice,
        long quantity,
        List<OptionProductDto> optionList
){
}