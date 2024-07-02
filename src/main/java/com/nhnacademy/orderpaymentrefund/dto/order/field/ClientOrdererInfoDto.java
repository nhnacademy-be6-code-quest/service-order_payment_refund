package com.nhnacademy.orderpaymentrefund.dto.order.field;

public record ClientOrdererInfoDto(
        String ordererName,
        String phoneNumber,
        String zipCode,
        String address,
        String detailAddress
){
}
