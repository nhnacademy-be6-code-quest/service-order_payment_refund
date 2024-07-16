package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;

public class PaymentNotCompletedException extends NotFoundExceptionType {
    public PaymentNotCompletedException(String message) {
        super(message);
    }
}
