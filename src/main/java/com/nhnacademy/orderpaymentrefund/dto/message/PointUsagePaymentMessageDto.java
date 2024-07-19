package com.nhnacademy.orderpaymentrefund.dto.message;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointUsagePaymentMessageDto {
    Long clientId;
    Long pointUsagePayment;
}
