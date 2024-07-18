package com.nhnacademy.orderpaymentrefund.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryIncreaseRequestDto {
    Long productId;
    Long quantityToIncrease;
}
