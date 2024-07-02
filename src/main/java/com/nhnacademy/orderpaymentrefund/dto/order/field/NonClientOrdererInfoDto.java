package com.nhnacademy.orderpaymentrefund.dto.order.field;

import lombok.Builder;

@Builder
public record NonClientOrdererInfoDto(
        String phoneNumber,
        String zipCode,
        String address,
        String detailAddress,
        String nonClientOrderPassword,
        String nonClientOrdererName,
        String nonClientOrdererEmail
){
}
