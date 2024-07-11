package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;

public class ProductOrderDetailNotFoundException extends NotFoundExceptionType {
    public ProductOrderDetailNotFoundException() {
        super("productOrderDetail을 찾지 못했습니다.");
    }
    public ProductOrderDetailNotFoundException(String message) {
        super(message);
    }
}
