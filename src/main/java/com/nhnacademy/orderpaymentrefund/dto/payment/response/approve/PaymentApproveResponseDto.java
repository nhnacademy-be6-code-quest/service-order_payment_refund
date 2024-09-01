package com.nhnacademy.orderpaymentrefund.dto.payment.response.approve;

public interface PaymentApproveResponseDto {
    default boolean isVirtualAccount(){
        return getSuccessPaymentOrderInfo().isVirtualAccount();
    }

    SuccessPaymentOrderInfo getSuccessPaymentOrderInfo();
    void setSuccessPaymentOrderInfo(SuccessPaymentOrderInfo successPaymentOrderInfo);
}
