package com.nhnacademy.order.exception;

import com.nhnacademy.order.exception.exception.NotFoundExceptionType;

public class ShippingPolicyNotFoundException extends NotFoundExceptionType {
    public ShippingPolicyNotFoundException() {
        super("배송 정책을 찾을 수 없습니다.");
    }
    public ShippingPolicyNotFoundException(String message) {
        super(message);
    }
}
