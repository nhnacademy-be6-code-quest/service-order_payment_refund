package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.ForbiddenExceptionType;

public class NonClientCannotAccessClientService extends ForbiddenExceptionType {
    public NonClientCannotAccessClientService(){
        super("비회원은 회원 서비스에 접근할 수 없습니다!");
    }
    public NonClientCannotAccessClientService(String message){
        super(message);
    }
}
