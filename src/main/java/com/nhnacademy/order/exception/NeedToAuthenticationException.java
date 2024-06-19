package com.nhnacademy.order.exception;

import com.nhnacademy.order.exception.exception.UnauthorizedExceptionType;

public class NeedToAuthenticationException extends UnauthorizedExceptionType {
    public NeedToAuthenticationException() {
        super("인증이 필요합니다");
    }
    public NeedToAuthenticationException(String message) {
        super(message);
    }
}
