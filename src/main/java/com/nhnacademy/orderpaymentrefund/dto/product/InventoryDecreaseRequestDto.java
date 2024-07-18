package com.nhnacademy.orderpaymentrefund.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDecreaseRequestDto {
    Long productId;
    Long quantityToDecrease;
}
