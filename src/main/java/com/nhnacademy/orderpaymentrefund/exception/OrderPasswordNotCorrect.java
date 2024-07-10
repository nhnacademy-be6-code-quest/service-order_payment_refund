package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class OrderPasswordNotCorrect extends BadRequestExceptionType {
    public OrderPasswordNotCorrect(){
        super("비회원 주문 비밀번호가 일치하지 않습니다");
    }
    public OrderPasswordNotCorrect(String message) {
        super(message);
    }
}
