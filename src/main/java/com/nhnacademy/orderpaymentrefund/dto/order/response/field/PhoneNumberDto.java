package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.Builder;

@Builder
public record PhoneNumberDto (
    String alias,
    String phoneNumber
)
{

}
