package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;

public class CannotCancelPaymentCancel extends BadRequestExceptionType {

    public CannotCancelPaymentCancel(String message) {
        super(message);
    }
}
