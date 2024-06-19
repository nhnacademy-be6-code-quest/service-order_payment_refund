package com.nhnacademy.order.dto.order.response.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItemDto {
    private long id;
    private String imgPath;
    private String name;
    private long price;
    private long quantity;
}
