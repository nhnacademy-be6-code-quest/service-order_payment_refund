package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record PhoneNumberDto (
    String alias,
    String phoneNumber
)
{

}
