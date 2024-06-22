package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.UnauthorizedExceptionType;

public class NeedToAuthenticationException extends UnauthorizedExceptionType {
    public NeedToAuthenticationException() {
        super("인증이 필요합니다");
    }
    public NeedToAuthenticationException(String message) {
        super(message);
    }
}
