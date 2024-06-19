package com.nhnacademy.order.dto.order.response.client;

import com.nhnacademy.order.dto.order.response.field.PackageItemDto;
import com.nhnacademy.order.dto.order.response.field.ProductItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderPostResponseDto { // 주문 페이지 화면에 뿌려질 데이터
    private List<ProductItemDto> productItemDtoList;
    private List<PackageItemDto> packageItemDtoList;
    private long shippingFee;
    private long minPurchasePrice;
    private String shippingPolicyName;
}
