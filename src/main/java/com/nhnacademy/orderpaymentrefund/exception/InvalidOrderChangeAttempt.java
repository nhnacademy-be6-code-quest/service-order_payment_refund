package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class InvalidOrderChangeAttempt extends BadRequestExceptionType {
    public InvalidOrderChangeAttempt(){
        super("잘못된 상태변경 시도입니다.");
    }
    public InvalidOrderChangeAttempt(String message){
        super(message);
    }
}
