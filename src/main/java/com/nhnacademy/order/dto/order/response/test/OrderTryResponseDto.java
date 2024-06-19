package com.nhnacademy.order.dto.order.response.test;

import com.nhnacademy.order.dto.order.response.field.PackageItemDto;
import com.nhnacademy.order.dto.order.response.field.ProductItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTryResponseDto {
    private List<ProductItemDto> productItemDtoList;
    private List<PackageItemDto> packageItemDtoList;
    private long shippingPrice;
    private long minPurchasePrice;
    private String shippingPolicyName;
}
