package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;

public class RefundPolicyNotFoundException extends NotFoundExceptionType {

    public RefundPolicyNotFoundException(String message) {
        super(message);
    }
}
