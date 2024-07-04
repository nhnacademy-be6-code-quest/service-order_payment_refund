package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class FailToPreProcessing extends BadRequestExceptionType {
    public FailToPreProcessing() {
        super("주문 및 결제 전처리 실패");
    }
    public FailToPreProcessing(String message) {
        super(message);
    }
}
