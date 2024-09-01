package com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class TossPaymentApproveResponseDto implements PaymentApproveResponseDto {

    private String pgName = "toss";

    private String paymentKey;
    private String orderId; // orderCode
    private String orderName;
    private String method; // 카드, 가상계좌, 간편결제, 휴대폰 등..
    private Long totalAmount; // 결제금액
    private Boolean cultureExpense;
    private Long taxFreeAmount;
    private Integer taxExemptionAmount;
    private VirtualAccount virtualAccount;
    private Failure failure;

    @Setter
    private SuccessPaymentOrderInfo successPaymentOrderInfo;

    @NoArgsConstructor
    @Getter
    public static class VirtualAccount{
        private String accountNumber;
        private String bankCode;
        private LocalDateTime dueDate;
    }

    @NoArgsConstructor
    @Getter
    public static class Failure{
        private String code;
        private String message;
    }

    @Override
    public boolean isVirtualAccount() {
        return virtualAccount != null;
    }

}
