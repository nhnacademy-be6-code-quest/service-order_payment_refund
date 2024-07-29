package com.nhnacademy.orderpaymentrefund.dto.order.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateNonClientOrderPasswordRequestDto {
    String ordererName;
    String phoneNumber;
    String email;
    String newPassword;
}
