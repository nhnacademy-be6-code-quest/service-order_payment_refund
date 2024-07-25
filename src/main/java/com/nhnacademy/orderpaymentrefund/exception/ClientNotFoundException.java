package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;

public class ClientNotFoundException extends NotFoundExceptionType {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
