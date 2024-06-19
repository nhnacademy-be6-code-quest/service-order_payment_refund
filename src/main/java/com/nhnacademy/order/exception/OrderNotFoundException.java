package com.nhnacademy.order.exception;

import com.nhnacademy.order.exception.exception.NotFoundExceptionType;

public class OrderNotFoundException extends NotFoundExceptionType{
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }
    public OrderNotFoundException(String message) {
        super(message);
    }
}
