package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;

public class RefundImpossibleException extends NotFoundExceptionType {

    public RefundImpossibleException(String message) {
        super(message);
    }
}
