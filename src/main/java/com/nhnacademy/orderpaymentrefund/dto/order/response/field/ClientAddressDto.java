package com.nhnacademy.orderpaymentrefund.dto.order.response.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientAddressDto {
    private String addressNickname; // 주소별칭
    private String address; // 도로명 주소
    private String detailAddress; // 상세주소
    private String zipCode; // 우편번호
}
