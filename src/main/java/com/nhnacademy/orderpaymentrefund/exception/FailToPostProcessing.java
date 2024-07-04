package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class FailToPostProcessing extends BadRequestExceptionType {
    public FailToPostProcessing() {
        super("주문 및 결제 후처리에 실패하였습니다");
    }
    public FailToPostProcessing(String message) {
        super(message);
    }
}
