package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class CannotCancelOrder extends BadRequestExceptionType {
    public CannotCancelOrder() {
        super("주문 취소에 실패하였습니다");
    }
    public CannotCancelOrder(String message) {
        super(message);
    }
}
