package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class WrongClientAccessToOrder extends BadRequestExceptionType {
    public WrongClientAccessToOrder() {
        super("본인의 Order 데이터에 접근하지 않았습니다.");
    }
    public WrongClientAccessToOrder(String message) {
        super(message);
    }
}
