package com.nhnacademy.orderpaymentrefund.exception;

import com.nhnacademy.orderpaymentrefund.exception.type.ForbiddenExceptionType;

public class ClientCannotAccessNonClientService extends ForbiddenExceptionType {
    public ClientCannotAccessNonClientService(){
        super("회원은 비회원 서비스에 접근할 수 없습니다!");
    }
    public ClientCannotAccessNonClientService(String message){
        super(message);
    }
}
