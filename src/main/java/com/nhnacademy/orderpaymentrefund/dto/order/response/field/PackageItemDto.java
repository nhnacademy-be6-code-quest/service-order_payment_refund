package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackageItemDto {
    private long id;
    private String name;
    private long price;
}
