package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.Builder;

@Builder
public record ClientAddressDto (
    String addressNickname, // 주소별칭
    String address, // 도로명 주소
    String detailAddress, // 상세주소
    String zipCode // 우편번호
)
{

}
