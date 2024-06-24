package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;

public class PaymentNotFoundException extends NotFoundExceptionType {
    public PaymentNotFoundException() { super("결제 정보를 찾을 수 없습니다."); }
    public PaymentNotFoundException(String message) { super(message); }
}