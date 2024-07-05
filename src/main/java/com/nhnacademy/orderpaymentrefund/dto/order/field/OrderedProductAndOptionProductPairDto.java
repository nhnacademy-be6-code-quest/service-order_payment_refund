package com.nhnacademy.orderpaymentrefund.dto.order.field;

import lombok.Builder;

import java.util.List;

/**
 * OrderedProductAndOptionProductPairDto : 주문한 상품 디테일, 선택한 옵션 상품 리스트
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record OrderedProductAndOptionProductPairDto (
        ProductOrderDetailDto productOrderDetailDto,
        List<ProductOrderDetailOptionDto> productOrderDetailOptionDtoList
){
}