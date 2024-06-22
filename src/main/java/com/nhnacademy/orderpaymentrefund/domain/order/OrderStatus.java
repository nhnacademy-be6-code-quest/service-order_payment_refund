package com.nhnacademy.orderpaymentrefund.domain.order;

public enum OrderStatus
{
    WAIT_PAYMENT("결제대기", "wait"), DELIVERING("배송중", "delivering"), COMPLETE("배송완료", "complete"), REFUND("반품", "refund"), CANCEL("주문취소", "cancel");

    public final String korDescription;
    public final String enDescription;

    OrderStatus(String korDescription, String enDescription) {
        this.korDescription = korDescription;
        this.enDescription = enDescription;
    }

    public String getKorDescription() {
        return this.korDescription;
    }

    public String getEnDescription() {
        return this.enDescription;
    }

    @Override
    public String toString() {
        return this.getKorDescription();
    }
}