package com.nhnacademy.orderpaymentrefund.dto.payment.response.approve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class SuccessPaymentOrderInfo {

    private Long orderIdOnDB; // db상 주문 아이디
    private List<Long> productIdList; // 상품 아이디 리스트
    private String orderName; // 주문 이름
    private Long totalPayAmount; // 결제 금액
    private String deliveryAddress; // 배송지
    private String paymentMethodName; // 결제수단 - 카드, 계좌이체 등..
    private VirtualAccountInfo virtualAccountInfo; // 가상계좌 정보

    @AllArgsConstructor
    @Builder
    @Getter
    public static class VirtualAccountInfo {
        private String bank;
        private String account;
        private String depositDueDate;
    }

    public boolean isVirtualAccount(){
        return virtualAccountInfo != null;
    }

}
